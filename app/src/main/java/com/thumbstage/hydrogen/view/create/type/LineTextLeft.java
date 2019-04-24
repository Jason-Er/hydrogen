package com.thumbstage.hydrogen.view.create.type;

import com.thumbstage.hydrogen.model.bo.Line;

public class LineTextLeft extends Line {
    public LineTextLeft(Line line) {
        who = line.getWho();
        when = line.getWhen();
        what = line.getWhat();
        lineType = line.getLineType();
    }
}
