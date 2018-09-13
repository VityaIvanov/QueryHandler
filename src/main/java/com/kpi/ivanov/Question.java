package com.kpi.ivanov;

import java.util.Objects;

/**
 * Represents a question
 * Question has two fields type and QuestionCategory, questionCategory can be optional
 */
public final class Question {
    private int type;
    private QuestionCategory questionCategory;

    public Question(int type) {
        this.type = type;
        this.questionCategory = null;
    }

    public Question(int type, int category) {
        this.type = type;
        this.questionCategory = new QuestionCategory(category);
    }

    public Question(int type, int category, int subCategory) {
        this.type = type;
        this.questionCategory = new QuestionCategory(category, subCategory);
    }

    /**
     * Method that check matching of two question
     * @param question QueryEntry question
     * @return true if question is matches this obj.question
     */
    boolean isMatches(Question question) {
        if (question.questionCategory == null) {
            return type == question.type;
        }

        if (questionCategory != null) {
            return type == question.type &&
                    questionCategory.isMatches(question.questionCategory);
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Question)) {
            return false;
        }

        Question question = (Question) obj;

        if (this.questionCategory != null && question.questionCategory != null) {
            return this.type == question.type && this.questionCategory.equals(question.questionCategory);
        }

        if (this.questionCategory == null && question.questionCategory == null) {
            return this.type == question.type;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, questionCategory);
    }

    /**
     * Represents a question category
     * Question category has two fields type and subCategory, subCategory can be optional
     */
    public static class QuestionCategory {
        private int category;
        private Integer subCategory;

        QuestionCategory(int category, int subCategory) {
            this.category = category;
            this.subCategory = subCategory;
        }

        QuestionCategory(int category) {
            this.category = category;
            this.subCategory = null;
        }

        /**
         * Method that check matching of two questionCategory
         * @param questionCategory QueryEntry questionCategory
         * @return true if questionCategory is matches this obj.questionCategory
         */
        boolean isMatches(QuestionCategory questionCategory) {
            if (questionCategory.subCategory == null) {
                return category == questionCategory.category;
            }

            if (subCategory != null) {
                return category == questionCategory.category &&
                        subCategory.equals(questionCategory.subCategory);
            }

            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof QuestionCategory)) {
                return false;
            }

            QuestionCategory questionCategory = (QuestionCategory) obj;

            if (this.subCategory != null && questionCategory.subCategory != null) {
                return this.category == questionCategory.category && this.subCategory.equals(questionCategory.subCategory);
            }

            if (this.subCategory == null && questionCategory.subCategory == null) {
                return this.category == questionCategory.category;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(category, subCategory);
        }
    }
}
