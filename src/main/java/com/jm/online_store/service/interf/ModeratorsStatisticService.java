package com.jm.online_store.service.interf;

import com.jm.online_store.model.ModeratorsStatistic;
import com.jm.online_store.model.User;

import java.util.List;

public interface ModeratorsStatisticService {
    List<ModeratorsStatistic> findAll();
    void incrementDismissedCount(User moderator);
    void incrementApprovedCount(User moderator);
}
