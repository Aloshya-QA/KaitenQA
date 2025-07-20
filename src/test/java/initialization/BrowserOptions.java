package initialization;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Collections;
import java.util.HashMap;

@Log4j2
public class BrowserOptions {

    public static EdgeOptions getEdgeOptions() {
        log.info("Edge flags initialization");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--lang=en");
        options.addArguments("--start-maximized");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }

    public static ChromeOptions getChromeOptions() {
        log.info("Chrome flags initialization");
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--lang=en");
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }
}
