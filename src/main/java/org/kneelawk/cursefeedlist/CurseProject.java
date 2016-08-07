package org.kneelawk.cursefeedlist;

import java.util.List;

public class CurseProject {
	private int projectId;
	private String name;
	private String thumbnailUrl;
	private List<String> authors;

	public CurseProject(int projectId, String name, String thumbnailUrl, List<String> authors) {
		super();
		this.projectId = projectId;
		this.name = name;
		this.thumbnailUrl = thumbnailUrl;
		this.authors = authors;
	}

	public int getProjectId() {
		return projectId;
	}

	public String getName() {
		return name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public List<String> getAuthors() {
		return authors;
	}
}
