package com.AIExpense.elementtest.Record;

import java.util.LinkedList;
import java.util.Queue;

public class Transcription {
    private StringBuilder transcription;

    public Transcription() {
        transcription = new StringBuilder();
    }

    public void addTranscription(String transcription) {
        this.transcription.append(transcription);
    }

    public void clearTranscriptions() {
        transcription = new StringBuilder();
    }

    public String getTranscriptions() {
        return transcription.toString();
    }

    public String getTestTranscription() {
        return "User: 喂\\n; Assistant: 喂，你好！最近過得怎麼樣啊？最近有去什麼懷舊的地方走走嗎？; User: 問得真好,我昨天剛好回去了以前的學校,找了以前的同學\\n; Assistant: 真的嗎？聽起來很有趣呢！跟老同學見面一定聊了很多以前的趣事吧？你們是不是也一起吃飯了？吃了什麼好料呢？; User: 沒錯,我昨天跟同學一起吃飯了 不過我們每個人吃的東西不一樣 我自己花了110塊買了一碗滷味,很好吃喔\\n; Assistant: 滷味真的很不錯！而且110塊也算是很划算了。看來你昨天和同學聚會得很開心呢！今天有沒有什麼特別的計畫呢？; User: 今天沒有什麼特別的計畫\\n; Assistant: 有時候放鬆一點也不錯啦。對了，你昨天吃滷味外，有沒有買什麼飲料或其他的東西呢？最近想說幫你記一下花費嘛！; User: 沒有買飲料耶,因為昨天想說吃得健康一點 不過晚上倒是有花了160塊買了一碗臭臭鍋,也是很好吃\\n; Assistant: 喔，臭臭鍋也很好吃！而且160塊也算是合理的價位。真不錯，你最近的餐點都很豐富呢！加上中午的滷味，這樣昨天總共花了270塊，聽起來是不錯的一天啊！; User: 对啊,昨天真的过得蛮开心的\\n; Assistant: 這就好！跟朋友聚會又吃了美食，真的是很棒的回憶。如果今天想要繼續放鬆，也可以找個地方走走散散心。需要什麼建議或想聊聊都可以找我喔！; User: 好啊那我晚點再回來跟你講我今天買了什麼東西\\n; Assistant: 沒問題！隨時歡迎你回來跟我分享。先祝你今天愉快喔！; User: 謝謝你也祝你今天愉快 掰掰\\n; Assistant: 謝謝！你也要好好享受今天喔！掰掰！;";
    }
}
