package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class ParticleEffect {

	public static final int MAX_GENERATION = 3;

	public ArrayList<Particle> particles;
	public Vector2 emitPosition;

	public ParticleEffect(Vector2 emitPos) {
		particles = new ArrayList<>();
		emitPosition = emitPos;
	}

	public void update(float dt) {
		for (Particle p : particles) {
			p.update(dt);
		}
	}
	
	public void render(SpriteBatch b) {
		for (Particle p : particles) {
			p.render(b);
		}
	}

	public void play() {
		ArrayList<Particle> removals = new ArrayList<>();

		for (Particle p : particles) {
			p.generation++;
			if (p.generation >= MAX_GENERATION) {
				removals.add(p);
			}
		}

		for (Particle p : removals) {
			particles.remove(p);
		}
	}
}
