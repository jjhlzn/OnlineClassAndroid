package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.model.ZhuanLan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetQuestionResponse extends ServerResponse{
    private List<Question> mQuestions = new ArrayList<>();

    public List<Question> getQuestions() {
        return mQuestions;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        mQuestions = parse(jsonObject);
    }

    public static List<Question> parse(JSONObject jsonObject) throws JSONException {
        List<Question> questions = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("questions");
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Question question = new Question();
            question.setId(json.getString("id"));
            question.setUserId(json.getString("userId"));
            question.setUserName(json.getString("name"));
            question.setTime(json.getString("time"));
            question.setContent(json.getString("content"));
            question.setAnswerCount(json.getInt("answerCount"));
            question.setThumbCount(json.getInt("thumbCount"));
            question.setLiked(json.getBoolean("isLiked"));

            JSONArray array = json.getJSONArray("answers");
            for(int j = 0; j < array.length(); j++) {
                JSONObject jo = array.getJSONObject(j);
                Answer answer = new Answer();
                answer.setFromUserId(jo.getString("fromId"));
                answer.setFromUserName(jo.getString("fromName"));
                answer.setToUserId(jo.getString("toId"));
                answer.setToUserName(jo.getString("toName"));
                answer.setContent(jo.getString("content"));
                answer.setFromManager(jo.getBoolean("isFromManager"));
                question.getAnswers().add(answer);
            }
            questions.add(question);
        }
        return questions;
    }
}
