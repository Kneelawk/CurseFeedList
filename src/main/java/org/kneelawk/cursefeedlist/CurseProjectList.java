package org.kneelawk.cursefeedlist;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CurseProjectList {
	private Map<Integer, CurseProject> projects = Maps.newHashMap();
	private Map<String, CurseProject> projectNames = Maps.newHashMap();

	/**
	 * Parses required data from the feed json object. Newest objects should be
	 * parsed first to avoid adding outdated duplicates.
	 * 
	 * @param feed
	 *            is the JSONObject to be parsed into a CurseProject
	 */
	public void addProjectsFromJson(JSONObject feed, ProgressListener progress) {
		JSONArray data = feed.getJSONArray("data");
		int len = data.length();
		for (int i = 0; i < len; i++) {
			JSONObject projObj = data.getJSONObject(i);

			int projId = projObj.getInt("Id");
			// make sure not to overwrite anything because overwriting can be
			// really messy
			if (projects.containsKey(projId)) {
				// +1 because processing is complete for this project
				progress.progress(i + 1, len);
				continue;
			}

			String projName = projObj.getString("Name");

			List<String> projAuthors = Lists.newArrayList();
			JSONArray projAuthorsArr = projObj.getJSONArray("Authors");
			int authorsLen = projAuthorsArr.length();
			for (int j = 0; j < authorsLen; j++) {
				JSONObject author = projAuthorsArr.getJSONObject(j);
				projAuthors.add(author.getString("Name"));
			}

			String projThumbnail = null;
			if (!projObj.isNull("Attachments")) {
				JSONArray projAttach = projObj.getJSONArray("Attachments");
				JSONObject frstAttach = projAttach.getJSONObject(0);
				projThumbnail = frstAttach.getString("ThumbnailUrl");

				int attachLen = projAttach.length();
				if (attachLen > 1) {
					for (int j = 0; j < attachLen; j++) {
						JSONObject attachment = projAttach.getJSONObject(j);
						if (attachment.getBoolean("IsDefault")) {
							projThumbnail = attachment.getString("ThumbnailUrl");
						}
					}
				}
			}

			addCurseProject(new CurseProject(projId, projName, projThumbnail, projAuthors));

			// +1 because processing id complete for this project
			progress.progress(i + 1, len);
		}
	}

	public void addCurseProject(CurseProject proj) {
		// avoid overwriting values
		if (!projects.containsKey(proj.getProjectId())) {
			projects.put(proj.getProjectId(), proj);
			projectNames.put(proj.getName(), proj);
		}
	}
}
