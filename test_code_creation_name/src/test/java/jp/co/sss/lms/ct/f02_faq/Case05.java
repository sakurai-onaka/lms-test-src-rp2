package jp.co.sss.lms.ct.f02_faq;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
		setWaitTime();
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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//要素取得
		WebElement functionButton = webDriver.findElement(By.className("dropdown-toggle"));
		functionButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeClickHelp_1");
		//プルダウンのヘルプを取得
		webDriver.findElement(By.xpath("//a[text()='ヘルプ']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickHelp_2");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_HELP_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//よくある質問を取得
		webDriver.findElement(By.xpath(HTML_XPATH_A_TEXT_FAQ)).click();
		//別タブに切り替え
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[SEPARATE_TAB]);
		//エビデンス取得・テスト
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeClickFAQ_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_FAQ_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		WebElement keyword = webDriver.findElement(By.name("keyword"));
		WebElement searchButton = webDriver.findElement(By.xpath("//input[@value='検索']"));
		keyword.sendKeys(SEARCH_KEYWORD);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeClickSearch_1");
		searchButton.click();

		//検索結果欄にスクロールするために使用する要素
		WebElement searchBoxElem = webDriver.findElement(By.className("well"));
		int searchBoxheight = searchBoxElem.getSize().getHeight();
		WebDriverUtils.scrollBy(String.valueOf(searchBoxheight));
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickSearch_1");

		//検索結果に検索した文字列が含まれるかチェック
		List<WebElement> resultList = webDriver.findElements(By.tagName("dl"));
		for (WebElement result : resultList) {
			int height = result.getSize().getHeight();
			WebDriverUtils.scrollBy(String.valueOf(height));
			String sumText = result.findElement(By.tagName(HTML_TAGNAME_DT)).getText().substring(2);
			assertTrue(sumText.contains(SEARCH_KEYWORD));
		}
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		WebDriverUtils.scrollTo(ZERO_PIXEL);
		WebElement keyword = webDriver.findElement(By.name(HTML_NAME_KEYWORD));
		WebElement clearButton = webDriver.findElement(By.xpath(HTML_XPATH_INPUT_VALUE_CLEAR));
		getEvidence(new Object() {
		}, "beforeClickClear_1");
		clearButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickClear_1");
		assertTrue(keyword.getText().equals(""));
	}

}
