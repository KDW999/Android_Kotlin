package com.example.webcrawling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    data class MovieItem(
        val 제목 : String, // 영화 제목
        val 항목1 : String, // 평점
        val 항목2 : String, // 평점에 참여한 사람 수
        val 항목3 : String, // 예매율
        val 항목4 : String
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 크롤링 시작
        btnStart.setOnClickListener{
            doTask("http://encykorea.aks.ac.kr/Contents/SearchNavi?keyword=%EA%B3%A0%EB%A0%A4&ridx=0&tot=3928")
        }
    }

    // 크롤링 하기
    fun doTask(url : String){
        var documentTitle : String = ""
        var itemList : ArrayList<MovieItem> = arrayListOf() // MovieTiem 배열

        Single.fromCallable{
            try {
                //  사이트 젒고해서 HTML 문서 가져옴
                val doc = Jsoup.connect(url).get()

                // HTML 파싱해서 데이터 추출
                val elements : Elements = doc.select("div.wrap")
                // 여러개의 elements 처리

                run elemLoop@{
                    elements.forEachIndexed { index, elem ->
                        // elem은 하나의 li를 전달
                        var title = elem.select("h2.ko_title").text()
                        var num = elem.select("em.tit").text()
                        var num2 = elem.select("span.tx").text()
                        var num3 = elem.select("em.tit").text()
                        var reserve = elem.select("dl.info_exp div.star_t1 span.num").text()

                        // MovieItem 아이템 생성 후 추가
                        var item = MovieItem(title, num, num2, num3, reserve)
                        itemList.add(item)

                        // 10개만 가져오기
                        if (index == 9) return@elemLoop
                    }
                }

                documentTitle = doc.title()
            } catch (e : Exception) {e.printStackTrace()}
            return@fromCallable documentTitle
        }

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { text ->
                    showData(text.toString())

                    showData(itemList.joinToString())
                },
                { it.printStackTrace()})
    }


      // TextView에 출력
    fun showData(msg : String){
        output.append(msg + "\n")
      }
}