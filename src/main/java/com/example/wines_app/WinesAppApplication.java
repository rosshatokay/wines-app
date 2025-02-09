package com.example.wines_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.awt.*;
import java.net.URI;
import java.util.Locale;



@SpringBootApplication
public class WinesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WinesAppApplication.class, args);
		openBrowser("http://localhost:8080");
	}

	private static void openBrowser(String url) {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(new URI(url));
					System.out.println("🚀 Browser opened: " + url);
					return;
				}
			}

			// If Desktop API is not supported, use platform-specific commands
			String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
			Runtime runtime = Runtime.getRuntime();

			if (os.contains("win")) {
				runtime.exec("cmd /c start " + url);
			} else if (os.contains("mac")) {
				runtime.exec("open " + url);
			} else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
				runtime.exec("xdg-open " + url);
			} else {
				System.out.println("⚠️ Cannot open browser automatically. Please open manually: " + url);
			}
		} catch (Exception e) {
			System.err.println("❌ Failed to open browser: " + e.getMessage());
		}
	}

}