package com.roguelikedeckbuilder.mygame.helpers;

// To be able to type "(some variable name here).x"  or "(some variable name here).y"
public record XYPair<T>(T x, T y) {
}