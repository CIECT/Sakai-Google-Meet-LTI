package coza.opencollab.meetings.constant;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Action {


    SET_VIEW_FUTURE("viewFuture"),
    SET_VIEW_PAST("viewPast"),
    SET_LAYOUT_LIST("layoutList"),
    SET_LAYOUT_GRID("layoutGrid"),
    ;


    @Getter
    private final String param;


    public static Action getInstance(String param) {
        for (Action action : Action.values()) {
            if (StringUtils.equals(param, action.getParam())) {
                return action;
            }
        }

        throw new IllegalArgumentException("No action for param [" + param + "]");
    }
}
