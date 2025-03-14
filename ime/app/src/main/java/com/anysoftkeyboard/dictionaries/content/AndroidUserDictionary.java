/*
 * Copyright (c) 2013 Menny Even-Danan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anysoftkeyboard.dictionaries.content;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.UserDictionary.Words;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.anysoftkeyboard.base.utils.Logger;

public class AndroidUserDictionary extends ContentObserverDictionary {

  private static final String[] PROJECTION = {Words._ID, Words.WORD, Words.FREQUENCY};
  private final String mLocale;

  public AndroidUserDictionary(Context context, String locale) {
    this(context, locale, Words.CONTENT_URI);
  }

  protected AndroidUserDictionary(Context context, String locale, @Nullable Uri changeUri) {
    super("AndroidUserDictionary", context, changeUri);
    mLocale = locale;
  }

  @Override
  protected void readWordsFromActualStorage(WordReadListener listener) {
    @SuppressLint("Recycle")
    Cursor cursor =
        TextUtils.isEmpty(mLocale)
            ? mContext.getContentResolver().query(Words.CONTENT_URI, PROJECTION, null, null, null)
            : mContext
                .getContentResolver()
                .query(
                    Words.CONTENT_URI,
                    PROJECTION,
                    Words.LOCALE + "=?",
                    new String[] {mLocale},
                    null);

    if (cursor == null) throw new RuntimeException("No built-in Android dictionary!");
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast() && listener.onWordRead(cursor.getString(1), cursor.getInt(2))) {
        cursor.moveToNext();
      }
    }
    cursor.close();
  }

  @Override
  protected void addWordToStorage(String word, int frequency) {
    if (TextUtils.isEmpty(word)) {
      return;
    }

    if (frequency < 1) frequency = 1;
    if (frequency > 255) frequency = 255;

    ContentValues values = new ContentValues(4);
    values.put(Words.WORD, word);
    values.put(Words.FREQUENCY, frequency);
    values.put(Words.LOCALE, mLocale);
    values.put(Words.APP_ID, 0); // TODO: Get App UID

    Uri result = mContext.getContentResolver().insert(Words.CONTENT_URI, values);
    Logger.i(
        TAG,
        "Added the word '"
            + word
            + "' at locale "
            + mLocale
            + " into Android's user dictionary. Result "
            + result);
  }

  @Override
  protected final void deleteWordFromStorage(String word) {
    mContext.getContentResolver().delete(Words.CONTENT_URI, Words.WORD + "=?", new String[] {word});
  }

  @NonNull
  @Override
  public String toString() {
    return mLocale + "@" + super.toString();
  }

  @Override
  protected void closeStorage() {
    /*nothing to close here*/
  }
}
