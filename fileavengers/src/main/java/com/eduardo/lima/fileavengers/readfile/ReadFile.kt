/*
 *   Copyright 2021 Eduardo Lima
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.eduardo.lima.fileavengers.readfile

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class ReadFile(private val listener: ReadFileListener) {

    class Builder {
        fun build(listener: ReadFileListener) =
            ReadFile(listener = listener)
    }

    fun execute(filePath: String, fileName: String, charsetName: String = UTF8_ENCODING) {
        val file = File(filePath, fileName)
        if (!file.exists()) {
            listener.onFileNotAvailable()
            return
        }

        try {
            val inputStreamReader = InputStreamReader(FileInputStream(file), charsetName)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()

            listener.onSuccess(stringBuilder.toString())
        } catch (exception: Exception) {
            listener.onFailed(exception)
        }
    }

    companion object {
        const val UTF8_ENCODING = "utf8"
    }
}