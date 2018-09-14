package com.kpi.ivanov;

/**
 * Customers support question.
 */
final class Question {
    private int type;
    private QuestionCategory questionCategory;

    Question(int type) {
        this(type, null);
    }

    Question(int type, QuestionCategory questionCategory) {
        if (type <= 0 || type > 10) {
            throw new RuntimeException("Invalid value for question type " + type);
        }

        this.type = type;
        this.questionCategory = questionCategory;
    }

    boolean isMatches(Question question) {
        return type == question.type &&
                (question.questionCategory == null
                        || questionCategory == null
                        || questionCategory.isMatches(question.questionCategory));
    }

    /**
     * Customers support question category.
     */
    static final class QuestionCategory {
        private int category;
        private Integer subCategory;

        QuestionCategory(int category) {
            if (category <= 0 || category > 20) {
                throw new RuntimeException("Invalid value for question category " + category);
            }
            this.category = category;
            this.subCategory = null;
        }

        QuestionCategory(int category, int subCategory) {
            if (category <= 0 || category > 20) {
                throw new RuntimeException("Invalid value for question category " + category);
            }

            if (subCategory <= 0 || subCategory > 5) {
                throw new RuntimeException("Invalid value for question subCategory " + subCategory);
            }

            this.category = category;
            this.subCategory = subCategory;
        }

        boolean isMatches(QuestionCategory questionCategory) {
            return category == questionCategory.category &&
                    (questionCategory.subCategory == null || questionCategory.subCategory.equals(subCategory));
        }
    }
}
