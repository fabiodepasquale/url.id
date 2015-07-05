package api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

public class BlacklistJSON extends JSON{
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String word;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<BlacklistEntity> words;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Integer code = null;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String message;

    public BlacklistJSON(String word, int code, String message) {
        this.word = word;
        this.code = code;
        this.message = message;
    }

    public BlacklistJSON(List<BlacklistEntity> words) {
        this.words = words;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public List<BlacklistEntity> getWords() {
        return words;
    }
    public void setWords(List<BlacklistEntity> words) {
        this.words = words;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
