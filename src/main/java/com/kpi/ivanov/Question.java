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
        checkType(type);

        this.type = type;
        this.questionCategory = questionCategory;
    }

    boolean isMatches(Question question) {
        return type == question.type &&
                (question.questionCategory == null
                        || questionCategory == null
                        || questionCategory.isMatches(question.questionCategory));
    }

    private static void checkType(int type) {
        if (type < 1 || type > 10) {
            throw new IllegalArgumentException("Invalid value for question type. " +
                    "The type can take values from 1 to 10 but not the " + type);
        }
    }

    /**
     * Customers support question category.
     */
    static final class QuestionCategory {
        private int category;
        private Integer subCategory;

        QuestionCategory(int category) {
            checkCategory(category);

            this.category = category;
            this.subCategory = null;
        }

        QuestionCategory(int category, int subCategory) {
            checkCategory(category);
            checkSubCategory(subCategory);

            this.category = category;
            this.subCategory = subCategory;
        }

        boolean isMatches(QuestionCategory questionCategory) {
            return category == questionCategory.category &&
                    (questionCategory.subCategory == null || questionCategory.subCategory.equals(subCategory));
        }

        private static void checkCategory(int category) {
            if (category < 1 || category > 20) {
                throw new IllegalArgumentException("Invalid value for service type. " +
                        "The type can take values from 1 to 20 but not the " + category);
            }
        }

        private static void checkSubCategory(int subCategory) {
            if (subCategory < 1 || subCategory > 5) {
                throw new IllegalArgumentException("Invalid value for service type. " +
                        "The variation can take values from 1 to 5 but not the " + subCategory);
            }
        }
    }
}
