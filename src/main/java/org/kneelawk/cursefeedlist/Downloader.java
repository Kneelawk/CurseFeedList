package org.kneelawk.cursefeedlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

public class Downloader {
	/**
	 * Downloads content from the URL to the file output.
	 * A maximum progregress of -1 means that the maximum progress can't be determined.
	 * Besides the -1, maximum progress is unsigned.
	 * @param url is the place to download from
	 * @param output is the file to download to
	 * @param progress is the listener that keeps track of progress through the download task
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void downloadFile(String url, File output, ProgressListener progress)
			throws ClientProtocolException, IOException {
		try (FileOutputStream fos = new FileOutputStream(output)) {
			HttpGet get = new HttpGet(url);
			HttpResponse response = CurseFeedList.client().execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (status / 100 != 2) {
				throw new IOException("Invalid response code: " + status);
			}

			HttpEntity entity = response.getEntity();
			long downloaded = 0;
			long toDownload = entity.getContentLength();
			InputStream is = entity.getContent();

			byte[] buf = new byte[8192];
			int read;
			while ((read = is.read(buf)) >= 0) {
				fos.write(buf, 0, read);
				downloaded += read;
				if (progress != null)
					progress.progress(downloaded, toDownload);
			}
		}
	}

	public static String downloadString(String url, ProgressListener progress)
			throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = CurseFeedList.client().execute(get);
		int status = response.getStatusLine().getStatusCode();
		if (status / 100 != 2) {
			throw new IOException("Invalid response code: " + status);
		}

		HttpEntity entity = response.getEntity();
		long downloaded = 0;
		long toDownload = entity.getContentLength();
		InputStream is = entity.getContent();

		String content = "";

		// likely to be downloading much smaller content to string
		byte[] buf = new byte[256];
		int read;
		while ((read = is.read(buf)) >= 0) {
			content += new String(buf, 0, read);
			downloaded += read;
			if (progress != null)
				progress.progress(downloaded, toDownload);
		}

		return content;
	}
}
