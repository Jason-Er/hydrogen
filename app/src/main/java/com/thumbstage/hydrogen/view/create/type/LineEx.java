package com.thumbstage.hydrogen.view.create.type;

import com.thumbstage.hydrogen.model.vo.Line;

public class LineEx extends Line {

    public LineEx(Line line) {
        who = line.getWho();
        when = line.getWhen();
        what = line.getWhat();
        lineType = line.getLineType();
    }

    boolean needShowTime = true;

    public boolean isNeedShowTime() {
        return needShowTime;
    }

    public void setNeedShowTime(boolean needShowTime) {
        this.needShowTime = needShowTime;
    }
}
