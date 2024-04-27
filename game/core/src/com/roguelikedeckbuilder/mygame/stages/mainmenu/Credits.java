package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;

public class Credits extends Group {
    public Credits() {
        this.setTouchable(Touchable.disabled);
        this.setX(980);

        Label credits = LabelMaker.newLabel(
                """
                        - "Local Forecast - Elevator" Kevin MacLeod (incompetech.com) Licensed under Creative Commons: By Attribution 4.0 License http://creativecommons.org/licenses/by/4.0/

                        - "Autumn Warrior Remastered" by Darkkit10 (Newgrounds.com)

                        - "Ordinary" by zybor, Eurns (Newgrounds.com)

                        - "Blood Reaper Kane (Boss Theme)" by Dutonic (Newgrounds.com)""",
                LabelMaker.getSmall());

        credits.setAlignment(Align.topLeft);
        credits.setWidth(450);
        credits.setHeight(840);
        credits.setY(12);
        this.addActor(credits);

        Label creditsTitle = LabelMaker.newLabel("Credits", LabelMaker.getMedium());
        creditsTitle.setAlignment(Align.topLeft);
        creditsTitle.setY(credits.getY() + credits.getHeight() + 12);
        this.addActor(creditsTitle);
    }
}
