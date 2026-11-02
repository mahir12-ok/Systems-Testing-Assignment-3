import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class F6Testing {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Ensure an item is in the cart first
            driver.get("https://opencart.abstracta.us/index.php?route=product/product&product_id=40");
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-cart")));
            addToCartBtn.click();
            
            // Wait for cart notification
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-success")));

            // Navigate to Checkout Page
            driver.get("https://opencart.abstracta.us/index.php?route=checkout/checkout");

            // ~ Test Scenario 1: Select Guest Checkout Option ~
            WebElement guestRadio = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@value='guest']")
            ));
            if (!guestRadio.isSelected()) {
                guestRadio.click();
            }

            WebElement step1Continue = driver.findElement(By.id("button-account"));
            step1Continue.click();

            // Verify Step 2 (Billing Details) form expands and first name field is interactive
            WebElement firstNameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("input-payment-firstname")));
            if (firstNameInput.isDisplayed()) {
                System.out.println("Test Scenario 1 (Guest Checkout Selection): PASSED");
            } else {
                System.out.println("Test Scenario 1 (Guest Checkout Selection): FAILED");
            }

            Thread.sleep(1000);

            // ~ Test Scenario 2: Fill Guest Billing Information ~
            firstNameInput.sendKeys("Mahir");
            driver.findElement(By.id("input-payment-lastname")).sendKeys("Daiyan");
            driver.findElement(By.id("input-payment-email")).sendKeys("mahir.test" + System.currentTimeMillis() + "@example.com");
            driver.findElement(By.id("input-payment-telephone")).sendKeys("0123456789");
            driver.findElement(By.id("input-payment-address-1")).sendKeys("123 Test Street");
            driver.findElement(By.id("input-payment-city")).sendKeys("Sydney");
            driver.findElement(By.id("input-payment-postcode")).sendKeys("2000");

            // Select Country
            Select countrySelect = new Select(driver.findElement(By.id("input-payment-country")));
            countrySelect.selectByVisibleText("Australia");

            // Explicit wait for the region dropdown options to update via AJAX
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[@id='input-payment-zone']/option[contains(text(), 'New South Wales')]")
            ));

            Select zoneSelect = new Select(driver.findElement(By.id("input-payment-zone")));
            zoneSelect.selectByVisibleText("New South Wales");

            WebElement step2Continue = driver.findElement(By.id("button-guest"));
            step2Continue.click();

            // Verify Step 4 (Delivery Method) button becomes clickable after AJAX transition
            WebElement deliveryBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("button-shipping-method")));

            if (deliveryBtn.isDisplayed()) {
                System.out.println("Test Scenario 2 (Billing Information Form): PASSED");
            } else {
                System.out.println("Test Scenario 2 (Billing Information Form): FAILED");
            }

            Thread.sleep(1000);

            // ~ Test Scenario 3: Shipping Method & Terms & Conditions Agreement ~
            // Click Delivery Method Continue
            deliveryBtn.click();

            // Wait for agree checkbox in Payment Method section
            WebElement agreeCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("agree")));
            if (!agreeCheckbox.isSelected()) {
                agreeCheckbox.click();
            }

            WebElement step5Continue = driver.findElement(By.id("button-payment-method"));
            step5Continue.click();

            // Verify Step 6 (Confirm Order) button becomes clickable
            WebElement confirmOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("button-confirm")));

            if (confirmOrderBtn.isDisplayed()) {
                System.out.println("Test Scenario 3 (Shipping & Terms Agreement): PASSED");
            } else {
                System.out.println("Test Scenario 3 (Shipping & Terms Agreement): FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }
}