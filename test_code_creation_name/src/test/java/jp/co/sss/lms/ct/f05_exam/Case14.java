package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 試験実施機能
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果50点")
public class Case14 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
		WebElement loginId = webDriver.findElement(By.name(HTML_NAME_LOGINID));
		WebElement password = webDriver.findElement(By.name(HTML_NAME_PASS));
		WebElement loginButton = webDriver.findElement(By.xpath(HTML_XPATH_INPUT_VALUE_LOGIN));

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		List<WebElement> trElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		WebElement targetElem = null;
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains("試験有")) {
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
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		webDriver.findElement(By.xpath("//input[@value='詳細']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "examStart");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_EXAM_START_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		webDriver.findElement(By.xpath("//input[@value='試験を開始する']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "examQuestion");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_EXAM_QUESTION_URL);
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		//問題ごとにスクロールするために使用する要素
		List<WebElement> questionElems = webDriver.findElements(By.className("panel-default"));

		int count = 0;

		for (WebElement questionElem : questionElems) {
			switch (count) {
			case 0:
				webDriver.findElement(By.id("answer-0-2")).click();
				break;
			case 1:
				webDriver.findElement(By.id("answer-1-2")).click();
				break;
			case 2:
				webDriver.findElement(By.id("answer-2-0")).click();
				break;
			case 3:
				webDriver.findElement(By.id("answer-3-0")).click();
				break;
			case 4:
				webDriver.findElement(By.id("answer-4-1")).click();
				break;
			case 5:
				webDriver.findElement(By.id("answer-5-1")).click();
				break;
			case 6:
				webDriver.findElement(By.id("answer-6-0")).click();
				break;
			case 7:
				webDriver.findElement(By.id("answer-7-3")).click();
				break;
			case 8:
				webDriver.findElement(By.id("answer-8-0")).click();
				break;
			case 9:
				webDriver.findElement(By.id("answer-9-0")).click();
				break;
			case 10:
				webDriver.findElement(By.id("answer-10-0")).click();
				break;
			case 11:
				webDriver.findElement(By.id("answer-11-0")).click();
				break;

			}
			webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
			getEvidence(new Object() {
			}, "examQuestion_" + count);

			int searchBoxheight = questionElem.getSize().getHeight();
			searchBoxheight += 20;
			WebDriverUtils.scrollBy(String.valueOf(searchBoxheight));
			count++;
		}
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "examQuestion");
		webDriver.findElement(By.xpath("//input[@value='確認画面へ進む']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "examQuestion");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_EXAM_ANSWERCHECK_URL);
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		getExamEvidence(new Object() {
		}, "examCheck_");
		WebDriverUtils.scrollBy("5000");
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeExamAnswerCheck");
		//回答時間が1秒未満だとDBの回答時間がnull値になりシステムエラーが発生するためその対策
		Thread.sleep(1000);
		webDriver.findElement(By.id("sendButton")).click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		getExamEvidence(new Object() {
		}, "examAnswer_");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_EXAM_RESULT_URL);
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		WebDriverUtils.scrollBy("5000");
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeExamResult");
		webDriver.findElement(By.xpath("//input[@value='戻る']")).submit();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterExamAnswerResult");

		//本日の取得
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		String todayString = today.format(formatter);
		//テストコード実行時の日付と合致するテスト結果があるか確認
		List<WebElement> tdElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		WebElement targetElem = null;
		for (WebElement tdElem : tdElems) {
			if (tdElem.getText().contains(todayString) && tdElem.getText().contains("0.0点")) {
				targetElem = tdElem;
				break;
			}
		}
		assertNotNull(targetElem);
	}

}
