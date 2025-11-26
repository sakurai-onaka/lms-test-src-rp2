package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能②
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		//パスワード変更警告ダイアログ消去処理
		final Map<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);
		chromePrefs.put("profile.password_manager_leak_detection", false);

		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		webDriver = new ChromeDriver(chromeOptions);
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo(LMS_LOGIN_URL);
		// 各画面表示時に10秒待機する
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "transitionLogin_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LOGIN_URL);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		//要素取得
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		WebElement password = webDriver.findElement(By.name("password"));
		WebElement loginButton = webDriver.findElement(By.xpath("//input[@value='ログイン']"));

		//要素入力
		loginId.sendKeys(FIRST_LOGIN_STUDENT_ID);
		password.sendKeys(FIRST_LOGIN_STUDENT_ID);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeLogin_1");
		loginButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterLogin_2");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		WebElement nextToButton = webDriver.findElement(By.xpath("//button[text()='次へ']"));
		WebElement agreeCheckBox = webDriver.findElement(By.xpath("//input[@type='checkbox']"));
		WebDriverUtils.scrollBy("1000");
		agreeCheckBox.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "before_agree");
		nextToButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "after_agree");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_PASSWORD_CHANGE_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		//パスワード入力
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		currentPassword.sendKeys(FIRST_LOGIN_STUDENT_ID);
		password.sendKeys("System3sss");
		passwordConfirm.sendKeys("System3sss");

		webDriver.findElement(By.xpath("//button[@type='submit']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		webDriver.findElement(By.xpath("//button[@id='upd-btn']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "after_update");

		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_COURSE_DETAIL_URL);
	}

}
