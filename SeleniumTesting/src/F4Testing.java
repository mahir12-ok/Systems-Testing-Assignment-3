import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class F4Testing {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Direct URL for iPhone Product Details page on OpenCart Demo
            driver.get("https://opencart.abstracta.us/index.php?route=product/product&product_id=40");

            // --- Test Scenario 1: Add 1 Item to Cart ---
            WebElement quantityInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-quantity")));
            quantityInput.clear();
            quantityInput.sendKeys("1");

            WebElement addToCartBtn = driver.findElement(By.id("button-cart"));
            addToCartBtn.click();

            // Verify success banner notification appears
            WebElement cartAlert = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".alert-success")
            ));

            if (cartAlert.getText().contains("Success: You have added iPhone to your shopping cart!")) {
                System.out.println("Test Scenario 1 (Add 1 Item to Cart): PASSED");
            } else {
                System.out.println("Test Scenario 1 (Add 1 Item to Cart): FAILED");
            }

            Thread.sleep(1500);

            // --- Test Scenario 2: Switch to Product Reviews Tab ---
            WebElement reviewTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href, '#tab-review')]")
            ));
            reviewTab.click();

            WebElement reviewForm = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form-review")));

            if (reviewForm.isDisplayed()) {
                System.out.println("Test Scenario 2 (Switch to Reviews Tab): PASSED");
            } else {
                System.out.println("Test Scenario 2 (Switch to Reviews Tab): FAILED");
            }

            Thread.sleep(1500);

            // --- Test Scenario 3: Add Product to Compare ---
            WebElement compareBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@data-original-title='Compare this Product' or contains(@onclick, 'compare.add')]")
            ));
            compareBtn.click();

            // Verify success alert notification appears for product comparison
            WebElement compareAlert = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".alert-success")
            ));

            String alertText = compareAlert.getText().toLowerCase();
            if (alertText.contains("success") && alertText.contains("comparison")) {
                System.out.println("Test Scenario 3 (Add to Product Compare): PASSED");
            } else {
                System.out.println("Test Scenario 3 (Add to Product Compare): FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }
}