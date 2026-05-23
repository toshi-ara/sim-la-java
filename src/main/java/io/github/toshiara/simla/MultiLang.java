package io.github.toshiara.simla;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;


public final class MultiLang {
    private MultiLang() {
    }

    public static final Map<String, String> lang;
    static {
        Map<String, String> tempMap = new LinkedHashMap<>();
        tempMap.put("English", "en");
        tempMap.put("日本語", "ja");
        tempMap.put("中文（繁体）", "zh-TW");
        lang = Collections.unmodifiableMap(tempMap);
    }

    public static final Map<String, String> start = Map.of(
        "en", "Start",
        "ja", "開始",
        "zh-TW", "開始"
    );

    public static final Map<String, String> restart = Map.of(
        "en", "Restart",
        "ja", "再開",
        "zh-TW", "再開"
    );

    public static final Map<String, String> pause = Map.of(
        "en", "Pause",
        "ja", "一時停止",
        "zh-TW", "一時停止"
    );

    public static final Map<String, String> newexp = Map.of(
        "en", "New Exp.",
        "ja", "新規実験",
        "zh-TW", "新規實驗"
    );

    public static final Map<String, String> save = Map.of(
        "en", "Save",
        "ja", "保存",
        "zh-TW", "保存"
    );

    public static final Map<String, String> quit = Map.of(
        "en", "Quit",
        "ja", "終了",
        "zh-TW", "終了"
    );

    public static final Map<String, String> speed = Map.of(
        "en", "Speed",
        "ja", "倍速",
        "zh-TW", "倍速"
    );

    public static final Map<String, String> withResponse = Map.of(
        "en", "Respond",
        "ja", "反応あり",
        "zh-TW", "有反應"
    );

    public static final Map<String, String> withoutResponse = Map.of(
        "en", "Not respond",
        "ja", "反応なし",
        "zh-TW", "沒反應"
    );

    public static final Map<String, String> msgNewExp = Map.of(
        "en", "Do you want to start a new experiment?",
        "ja", "新規実験を行いますか?",
        "zh-TW", "要開始新的實驗嗎?"
    );

    public static final Map<String, String> msgQuit = Map.of(
        "en", "Do you want to quit?",
        "ja", "終了しますか?",
        "zh-TW", "確定要退出嗎?"
    );

    public static final Map<String, String> msgConfirm = Map.of(
        "en", "Confirm",
        "ja", "確認",
        "zh-TW", "確認"
    );

    public static final Map<String, String> msgYes = Map.of(
        "en", "Yes",
        "ja", "はい",
        "zh-TW", "是"
    );

    public static final Map<String, String> msgNo = Map.of(
        "en", "No",
        "ja", "いいえ",
        "zh-TW", "否"
    );


    public static final Map<String, String> msgFilename = Map.of(
        "en", "File Name (N):",
        "ja", "ファイル名 (N):",
        "zh-TW", "文件名 (N):"
    );

    public static final Map<String, String> msgFiletype = Map.of(
        "en", "Files of Type (T):",
        "ja", "ファイルの種類 (T):",
        "zh-TW", "文件類型 (T):"
    );

    public static final Map<String, String> fileFilterAll = Map.of(
        "en", "All Files (*.*)",
        "ja", "すべてのファイル (*.*)",
        "zh-TW", "所有文件 (*.*)"
    );

    public static final Map<String, String> fileFilterCsv = Map.of(
        "en", "CSV file (*.csv)",
        "ja", "CSVファイル (*.csv)",
        "zh-TW", "CSV文件 (*.csv)"
    );

    public static final Map<String, String> msgSave = Map.of(
        "en", "Save",
        "ja", "保存",
        "zh-TW", "保存"
    );

    public static final Map<String, String> msgCancel = Map.of(
        "en", "Cancel",
        "ja", "キャンセル",
        "zh-TW", "取消"
    );

    public static final Map<String, String> filechooserSaveCsv = Map.of(
        "en", "Save CSV file",
        "ja", "CSVファイルの保存",
        "zh-TW", "儲存CSV文件"
    );

    public static final Map<String, String> msgOverwiteConfirm = Map.of(
        "en", "File already exists. Overwrite?",
        "ja", "既に同じ名前のファイルが存在します。上書きしますか？",
        "zh-TW", "文件已存在。是否覆蓋？"
    );

    public static final Map<String, String> titleOverwiteConfirm = Map.of(
        "en", "Overwrite Confirm",
        "ja", "上書き確認",
        "zh-TW", "覆蓋確認"
    );

    public static final Map<String, String> msgSaveSuccess = Map.of(
        "en", "The file has been saved",
        "ja", "ファイルを保存しました",
        "zh-TW", "文件已儲存"
    );

    public static final Map<String, String> msgSaveFailed = Map.of(
        "en", "An error occurred while saving the file.",
        "ja", "ファイルの保存中にエラーが発生しました",
        "zh-TW", "保存文件時發生錯誤"
    );

    public static final Map<String, String> msgError = Map.of(
        "en", "Error",
        "ja", "エラー",
        "zh-TW", "錯誤"
    );
}
