package com.dhkim.timecapsule

import com.dhkim.timecapsule.common.DateUtil
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun containsSpecialCharacters(input: String): Boolean {
        val specialCharactersPattern = Regex("[^a-zA-Z0-9 ]") // 알파벳, 숫자, 공백 제외한 모든 문자
        return specialCharactersPattern.containsMatchIn(input)
    }

    @Test
    fun addition_isCorrect() {
        //assertEquals(4, 2 + 2)

        val testStrings = listOf(
            "HelloWorld123",
            "Hello World_",
            "NoSpecialChar",
            "Special@Char#Test"
        )

        for (string in testStrings) {
            println("Does \"$string\" contain special characters? ${containsSpecialCharacters(string)}")
        }
    }

    @Test
    fun `날짜 차이 테스트` () {
        val gap = DateUtil.getDateGap("2024-06-4")
        println("gap : $gap")
    }
}