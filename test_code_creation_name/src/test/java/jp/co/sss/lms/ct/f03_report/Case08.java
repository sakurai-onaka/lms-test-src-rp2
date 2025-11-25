package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
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
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//要素取得
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		WebElement password = webDriver.findElement(By.name("password"));
		WebElement loginButton = webDriver.findElement(By.xpath("//input[@value='ログイン']"));

		//要素入力
		loginId.sendKeys(EXIST_STUDENT_ID);
		password.sendKeys(EXIST_STUDENT_PASS);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeLogin_1");
		loginButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterLogin_2");

		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_COURSE_DETAIL_URL);
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		List<WebElement> trElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		WebElement targetElem = null;
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains("提出済み")) {
				targetElem = trElem;
				break;
			}
		}
		targetElem = targetElem.findElement(By.xpath(".//input[@value='詳細']"));
		targetElem.submit();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "sectionDetail");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_SECTION_DETAIL_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		webDriver.findElement(By.xpath("//input[contains(@value,'日報')]")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "transitionReportRegist");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_REPORT_REGIST_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		WebElement textElem = webDriver.findElement(By.tagName("textarea"));
		textElem.clear();
		textElem.sendKeys("修正後");
		getEvidence(new Object() {
		}, "BeforeReportRegist");
		webDriver.findElement(By.xpath("//button[contains(text(),'提出する')]")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterReportRegist");
		String nowURL = webDriver.getCurrentUrl();
		assertTrue(nowURL.contains(LMS_SECTION_DETAIL_URL));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		webDriver.findElement(By.xpath("//a[@href='/lms/user/detail']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterHello");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_USER_DETAIL_URL);
		
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		WebDriverUtils.scrollBy("1000");
		List<WebElement> trElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		WebElement targetElem = null;
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains("2022年10月1日") && trElem.getText().contains("日報")) {
				targetElem = trElem;
				break;
			}
		}
		targetElem = targetElem.findElement(By.xpath(".//input[@value='詳細']"));
		targetElem.submit();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "reportDetail");

		List<WebElement> tdElems = webDriver.findElements(By.xpath("//table/tbody/tr/td"));
		for (WebElement tdElem : tdElems) {
			if (tdElem.getText().contains("修正後")) {
				targetElem = tdElem;
				break;
			}
		}
		
		String updateReport = targetElem.getText();
		assertEquals(updateReport, "修正後");
	}

}
