package com.fwcd.harmony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fwcd.harmony.utils.BoolMap;

public enum PitchClass {
	C(0, false, 'C'),
	C_SHARP_D_FLAT(1, true, 'C', 'D'),
	D(2, false, 'D'),
	D_SHARP_E_FLAT(3, true, 'D', 'E'),
	E(4, false, 'E'),
	F(5, false, 'F'),
	F_SHARP_G_FLAT(6, true, 'F', 'G'),
	G(7, false, 'G'),
	G_SHARP_A_FLAT(8, true, 'G', 'A'),
	A(9, false, 'A'),
	A_SHARP_B_FLAT(10, true, 'A', 'B'),
	B(11, false, 'B');

	private static final Map<Character, BoolMap<PitchClass>> LOOKUP_TABLE = new HashMap<>();

	static {
		for (PitchClass pitchClass : values()) {
			for (char character : pitchClass.characters) {
				LOOKUP_TABLE.putIfAbsent(character, new BoolMap<>());
				LOOKUP_TABLE.get(character).put(pitchClass.isSemitone, pitchClass);
			}
		}
	}

	private final int index;
	private final char[] characters;
	private final boolean isSemitone;
	private final List<NoteClass> noteClasses = new ArrayList<>();

	private PitchClass(int index, boolean isSemitone, char... characters) {
		this.index = index;
		this.characters = characters;
		this.isSemitone = isSemitone;
	}

	public PitchClass plusHalfSteps(int halfSteps) {
		PitchClass[] values = values();
		return values[Math.floorMod(ordinal() + halfSteps, values.length)];
	}

	public PitchClass halfStepDown() {
		return plusHalfSteps(-1);
	}

	public PitchClass halfStepUp() {
		return plusHalfSteps(1);
	}

	public int getPitchIndex() { return index; }

	public char[] getCharacters() { return characters; }

	public boolean isSemitone() { return isSemitone; }

	/**
	 * Returns the enharmonically equivalent NoteClass
	 * representations of this PitchClass.
	 */
	public List<NoteClass> getNoteClasses() { return Collections.unmodifiableList(noteClasses); }

	void addNoteClass(NoteClass noteClass) { noteClasses.add(noteClass); }

	public int halfStepsTo(PitchClass next) {
		int difference = next.index - index;
		if (next.index >= index) {
			return difference;
		} else {
			return values().length + difference;
		}
	}
}
