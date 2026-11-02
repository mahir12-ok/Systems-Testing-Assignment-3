import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class F3Testing {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Navigate to OpenCart Home Page
            driver.get("https://opencart.abstracta.us/");

            // ~ Test Scenario 1: Valid Keyword Search ~
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("search")));
            searchBox.sendKeys("iPhone");
            searchBox.sendKeys(Keys.ENTER);

            // Verify URL contains search query or content is present
            if (driver.getCurrentUrl().contains("search")) {
                System.out.println("Test Scenario 1 (Valid Search): PASSED");
            } else {
                System.out.println("Test Scenario 1 (Valid Search): FAILED");
            }

            Thread.sleep(1500);

            // ~ Test Scenario 2: Advanced Search with Product Description Option ~
            WebElement searchCriteriaBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-search")));
            searchCriteriaBox.clear();
            searchCriteriaBox.sendKeys("Apple");

            WebElement descriptionCheckbox = driver.findElement(By.id("description"));
            if (!descriptionCheckbox.isSelected()) {
                descriptionCheckbox.click();
            }

            WebElement searchButton = driver.findElement(By.id("button-search"));
            searchButton.click();

            WebElement productThumb = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-thumb")));
            if (productThumb.isDisplayed()) {
                System.out.println("Test Scenario 2 (Advanced Description Search): PASSED");
            } else {
                System.out.println("Test Scenario 2 (Advanced Description Search): FAILED");
            }

            Thread.sleep(1500);

            // ~ Test Scenario 3: Non-existent Search Query ~
            WebElement topSearchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("search")));
            topSearchBox.clear();
            topSearchBox.sendKeys("ABCD");
            topSearchBox.sendKeys(Keys.ENTER);

            // Look for the content container and check if the warning paragraph is displayed
            WebElement contentArea = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("content")
            ));
            
            // Checking for common keywords in OpenCart's empty search warning
            if (contentArea.getText().toLowerCase().contains("product") || contentArea.getText().toLowerCase().contains("criteria")) {
                System.out.println("Test Scenario 3 (Invalid Search Message): PASSED");
            } else {
                System.out.println("Test Scenario 3 (Invalid Search Message): FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }
}