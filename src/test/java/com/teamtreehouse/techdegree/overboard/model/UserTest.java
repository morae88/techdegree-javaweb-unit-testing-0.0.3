package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by morgan.welch on 3/31/2017.
 */
public class UserTest {

    private User questioner;
    private User answerer;
    private Board board;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        board = new Board("food");
        questioner = new User(board, "questioner");
        answerer = new User(board, "answerer");
        question = questioner.askQuestion("Question");
        answer = answerer.answerQuestion(question, "Answer");
    }

    @Test
    public void questionerReputationGoesUp5PointsWithUpVote() throws Exception {
        question.addUpVoter(answerer);

        assertEquals(5, questioner.getReputation());
    }

    @Test
    public void answererReputationGoesUp10PointsIfAnswerUpVoted() throws Exception {
        answer.addUpVoter(questioner);

        assertEquals(10, answerer.getReputation());
    }

    @Test
    public void acceptedAnswerGivesAnswerer15Points() throws Exception {
        questioner.acceptAnswer(answer);

        assertEquals(15, answerer.getReputation());
    }

    @Test
    public void upVotingNotAllowedByOriginalQuestioner() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        questioner.upVote(question);
    }

    @Test
    public void upVotingNotAllowedByOriginalAnswerer() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        answerer.upVote(answer);
    }

    @Test
    public void downVotingNotAllowedByOriginalQuestioner() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        answerer.downVote(answer);
    }

    @Test
    public void downVotingNotAllowedByOriginalAnswerer() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        answerer.downVote(answer);
    }

    @Test
    public void onlyOriginalQuestionerCanAcceptAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only questioner can accept this answer as it is their question");

        answerer.acceptAnswer(answer);
    }

    @Test
    public void downVotingAQuestionDoesNotAffectReputation() throws Exception {
        question.addDownVoter(answerer);

        assertEquals(0, questioner.getReputation());
    }

    @Test
    public void downVotingAnswerLosesAPoint() throws Exception {
        answer.addDownVoter(questioner);

        assertEquals(-1, answerer.getReputation());
    }

}