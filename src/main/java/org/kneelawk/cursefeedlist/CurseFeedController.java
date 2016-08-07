package org.kneelawk.cursefeedlist;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class CurseFeedController {

	public static final int PAGE_LENGTH = 100;

	private CurseProjectList list = null;
	private File baseDir;
	private File feedDir;
	private JProgressBar progressBar;

	public CurseFeedController(File baseDir) {
		this.baseDir = baseDir;
		this.feedDir = new File(baseDir, "feed");
	}

	public void startDownload() {
		if (progressBar != null) {
			try {
				SwingUtilities.invokeAndWait(() -> progressBar.setMinimum(0));
			} catch (InvocationTargetException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		try {
			list = CurseFeedDownloader.downloadCurseProjectList(feedDir, new ProgressListener() {
				@Override
				public void progress(long current, long max) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (progressBar != null) {
								progressBar.setMaximum((int) max);
								progressBar.setValue((int) current);
							}
						}
					});
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setProgressBar(JProgressBar bar) {
		this.progressBar = bar;
	}
}
