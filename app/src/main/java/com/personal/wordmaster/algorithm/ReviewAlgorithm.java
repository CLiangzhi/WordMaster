package com.personal.wordmaster.algorithm;

import com.personal.wordmaster.constant.AppConstant;
import com.personal.wordmaster.data.entity.WordInfo;

public class ReviewAlgorithm {

    public static void updateAfterKnown(WordInfo word) {
        int known = word.getKnownCount() + 1;
        int unknown = word.getUnknownCount();
        int total = known + unknown;
        float masteryRate = (float) known / total;

        word.setKnownCount(known);
        word.setMasteryRate(masteryRate);

        if (masteryRate >= AppConstant.MASTERY_THRESHOLD) {
            word.setMasteryStatus(AppConstant.STATUS_MASTERED);
        } else {
            word.setMasteryStatus(AppConstant.STATUS_UNFAMILIAR);
        }

        long currentInterval = word.getNextReviewTime() - word.getLastStudyTime();
        if (currentInterval <= 0) {
            currentInterval = AppConstant.INTERVAL_KNOWN_FIRST;
        }
        long newInterval = Math.min(currentInterval * 2, AppConstant.INTERVAL_MAX);
        word.setNextReviewTime(System.currentTimeMillis() + newInterval);
        word.setLastStudyTime(System.currentTimeMillis());
    }

    public static void updateAfterUnknown(WordInfo word) {
        int known = word.getKnownCount();
        int unknown = word.getUnknownCount() + 1;
        int total = known + unknown;
        float masteryRate = (float) known / total;

        word.setUnknownCount(unknown);
        word.setMasteryRate(masteryRate);
        word.setMasteryStatus(AppConstant.STATUS_UNFAMILIAR);
        word.setNextReviewTime(System.currentTimeMillis() + AppConstant.INTERVAL_UNKNOWN_FIRST);
        word.setLastStudyTime(System.currentTimeMillis());
    }
}
