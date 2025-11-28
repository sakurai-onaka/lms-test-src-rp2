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
import org.openqa.selenium.support.ui.Select;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		webDriver.findElement(By.xpath(HTML_XPATH_A_HREF_DETAIL)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterHello");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LMS_USER_DETAIL_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebDriverUtils.scrollBy("1000");
		List<WebElement> trElems = webDriver.findElements(By.xpath(HTML_XPATH_TBODY_TR));
		WebElement targetElem = null;
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains(DATE_2020_10_2) && trElem.getText().contains(WEEKLYREPORT)) {
				targetElem = trElem;
				break;
			}
		}
		targetElem = targetElem.findElement(By.xpath(HTML_XPATH_CHILDELEM_INPUT_VALUE_DOFIX));
		targetElem.submit();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "reportDetail");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		learningContent.clear();
		learningContent.sendKeys(BLANK);
		//comprehensionLevel.clear();
		comprehensionLevel.selectByVisibleText(COMPREHENSION_LEVEL_ONE);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeLearningContentNull");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterLearningContentNull");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		learningContent.clear();
		learningContent.sendKeys(IT_LITERACY);
		//comprehensionLevel.clear();
		comprehensionLevel.selectByVisibleText(BLANK);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeComprehensionLevelNull");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterComprehensionLevelNull");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		WebElement AchievementLevelGoal = webDriver.findElement(By.id(HTML_ID_CONTENT_ZERO));
		learningContent.clear();
		learningContent.sendKeys(IT_LITERACY);
		comprehensionLevel.selectByVisibleText(COMPREHENSION_LEVEL_ONE);
		AchievementLevelGoal.clear();
		AchievementLevelGoal.sendKeys(ACHIEVEMENT_LEVEL_GOAL_ABNORMAL_BY_STRING_WORD);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeAchievementLevelGoalWithoutNum");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAchievementLevelGoalWithoutNum");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		WebElement AchievementLevelGoal = webDriver.findElement(By.id(HTML_ID_CONTENT_ZERO));
		learningContent.clear();
		learningContent.sendKeys(IT_LITERACY);
		comprehensionLevel.selectByVisibleText(COMPREHENSION_LEVEL_ONE);
		AchievementLevelGoal.clear();
		AchievementLevelGoal.sendKeys(ACHIEVEMENT_LEVEL_GOAL_ABNORMAL_BY_INT_WORD);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeAchievementLevelGoalOutBound");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAchievementLevelGoalOutBound");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		WebElement AchievementLevelGoal = webDriver.findElement(By.id(HTML_ID_CONTENT_ZERO));
		WebElement Impression = webDriver.findElement(By.id(HTML_ID_CONTENT_ONE));

		learningContent.clear();
		learningContent.sendKeys(IT_LITERACY);
		comprehensionLevel.selectByVisibleText(COMPREHENSION_LEVEL_ONE);
		AchievementLevelGoal.clear();
		Impression.clear();
		WebDriverUtils.scrollBy(ONE_HUNDRED_PIXEL);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeAchievementLevelGoalNull");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		WebDriverUtils.scrollBy(ONE_HUNDRED_PIXEL);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAchievementLevelGoalNull");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		//
		WebElement learningContent = webDriver.findElement(By.id(HTML_ID_LEANING_CONTENT));
		Select comprehensionLevel = new Select(webDriver.findElement(By.id(HTML_ID_COMPREHENSION_LEVEL)));
		WebElement AchievementLevelGoal = webDriver.findElement(By.id(HTML_ID_CONTENT_ZERO));
		WebElement Impression = webDriver.findElement(By.id(HTML_ID_CONTENT_ONE));
		WebElement lookingBackWeek = webDriver.findElement(By.id(HTML_ID_CONTENT_TWO));

		learningContent.clear();
		learningContent.sendKeys(IT_LITERACY);
		comprehensionLevel.selectByVisibleText(COMPREHENSION_LEVEL_ONE);
		AchievementLevelGoal.clear();
		AchievementLevelGoal.sendKeys(ACHIEVEMENT_LEVEL_GOAL_NORMAL_WORD);
		Impression.clear();
		Impression.sendKeys(TWO_THOUSAND_WORD);
		lookingBackWeek.clear();
		lookingBackWeek.sendKeys(TWO_THOUSAND_WORD);

		WebDriverUtils.scrollBy(ONE_HUNDRED_PIXEL);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeAchievementLevelGoalOver2000");
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.findElement(By.xpath(HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION)).click();
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAchievementLevelGoalOver2000");
	}
}
