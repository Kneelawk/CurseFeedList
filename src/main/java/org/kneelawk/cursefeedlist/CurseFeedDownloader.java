package org.kneelawk.cursefeedlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CurseFeedDownloader {

	private static final String CURSE_FEED = "http://clientupdate-v6.cursecdn.com/feed/addons/432/v10/";
	private static final String CURSE_COMPLETE_FEED = CURSE_FEED + "complete.json.bz2";
	private static final String CURSE_COMPLETE_FEED_TIMESTAMP = CURSE_COMPLETE_FEED + ".txt";
	private static final String CURSE_HOURLY_FEED = CURSE_FEED + "hourly.json.bz2";
	private static final String CURSE_HOURLY_FEED_TIMESTAMP = CURSE_HOURLY_FEED + ".txt";

	private static final String CURSE_COMPLETE_FEED_FILE = "complete.json.bz2";
	private static final String CURSE_COMPLETE_FEED_TIMESTAMP_FILE = CURSE_COMPLETE_FEED_FILE + ".timestamp";
	private static final String CURSE_HOURLY_FEED_FILE = "hourly.json.bz2";
	private static final String CURSE_HOURLY_FEED_TIMESTAMP_FILE = CURSE_HOURLY_FEED_FILE + ".timestamp";

	public static void downloadCurseFeed(File file, CurseFeed type, ProgressListener progress)
			throws ClientProtocolException, IOException {
		Downloader.downloadFile(type.getDownloadUrl(), file, progress);
	}

	public static long downloadCurseFeedTimestamp(CurseFeed type) throws ClientProtocolException, IOException {
		String timestamp = Downloader.downloadString(type.getTimestampUrl(), null);
		return Long.parseLong(timestamp);
	}

	public static JSONObject downloadCurseFeedJson(File feedDir, CurseFeed type, ProgressListener progress)
			throws IOException {
		File feedFile = new File(feedDir, type.getFile());
		File timestampFile = new File(feedDir, type.getTimestampFile());

		// load timestamp file
		long currentTimestamp = 0;
		try {
			currentTimestamp = Long.parseLong(FileIO.readStringFromFile(timestampFile));
		} catch (NumberFormatException | FileNotFoundException e) {
		}

		// download latest timestamp
		long newTimestamp = Long.MAX_VALUE;
		try {
			newTimestamp = downloadCurseFeedTimestamp(type);
		} catch (NumberFormatException e) {
			// must have changed timestamp format
			e.printStackTrace();
		}

		if (!feedFile.exists() || currentTimestamp < newTimestamp) {
			FileIO.writeToFile(timestampFile, String.valueOf(newTimestamp));
			downloadCurseFeed(feedFile, type, progress);
		}

		return new JSONObject(new JSONTokener(new BZip2CompressorInputStream(new FileInputStream(feedFile))));
	}

	public static CurseProjectList downloadCurseProjectList(File feedDir, ProgressListener progress)
			throws IOException {
		CurseProjectList list = new CurseProjectList();

		JSONObject jsonHourlyFeed = downloadCurseFeedJson(feedDir, CurseFeed.HOURLY,
				new UnsignedDownloadProgressFilter(progress, 0, 250));
		list.addProjectsFromJson(jsonHourlyFeed, new ProgressFilter(progress, 250, 250));

		JSONObject jsonCompleteFeed = downloadCurseFeedJson(feedDir, CurseFeed.COMPLETE,
				new UnsignedDownloadProgressFilter(progress, 500, 250));
		list.addProjectsFromJson(jsonCompleteFeed, new ProgressFilter(progress, 750, 250));

		return list;
	}

	public static enum CurseFeed {
		COMPLETE(CURSE_COMPLETE_FEED, CURSE_COMPLETE_FEED_TIMESTAMP, CURSE_COMPLETE_FEED_FILE,
				CURSE_COMPLETE_FEED_TIMESTAMP_FILE), HOURLY(CURSE_HOURLY_FEED, CURSE_HOURLY_FEED_TIMESTAMP,
						CURSE_HOURLY_FEED_FILE, CURSE_HOURLY_FEED_TIMESTAMP_FILE);

		private String downloadUrl;
		private String timestampUrl;
		private String file;
		private String timestampFile;

		private CurseFeed(String downloadUrl, String timestampUrl, String file, String timestampFile) {
			this.downloadUrl = downloadUrl;
			this.timestampUrl = timestampUrl;
			this.file = file;
			this.timestampFile = timestampFile;
		}

		public String getDownloadUrl() {
			return downloadUrl;
		}

		public String getTimestampUrl() {
			return timestampUrl;
		}

		public String getFile() {
			return file;
		}

		public String getTimestampFile() {
			return timestampFile;
		}
	}
}
