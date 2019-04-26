package com.thumbstage.hydrogen.view.create.type;

import com.thumbstage.hydrogen.model.bo.Line;

public class LineTextRight extends Line {
    public LineTextRight(Line line) {
        who = line.getWho();
        when = line.getWhen();
        what = line.getWhat();
        lineType = line.getLineType();
    }
}
