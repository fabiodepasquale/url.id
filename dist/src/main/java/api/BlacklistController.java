package api;

import core.Blacklist;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/v1/blacklist")
public class BlacklistController {

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody JSON addWordToBlacklist(@RequestParam(value="word") String word) throws IOException {
        int code;
        String message;

        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("");
        }

        if (Blacklist.addBEntry(word)) {
            code = HttpStatus.OK.value();
            message = HttpStatus.OK.getReasonPhrase();
            return new BlacklistJSON(word, code, message);
        }
        else {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            return new ErrorJSON(code, message);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public JSON getBlacklist() throws IOException{

        Blacklist bl = new Blacklist();
        if (!bl.getBWords()) {
            int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            String message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            return new ErrorJSON(code, message);
        }

        List<String> listWords = bl.bWords;
        Collections.sort(listWords);

        List<BlacklistEntity> words = new ArrayList<>();

        for (String word : listWords) {
            words.add(new BlacklistEntity(word));
        }

        return new BlacklistJSON(words);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody JSON missingParam() {
        return new ErrorJSON(new ErrorEntity("required", "Required", "parameter", "word"), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody JSON invalidParam() {
        return new ErrorJSON(new ErrorEntity("invalid", "Required", "parameter", "word"), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }
}