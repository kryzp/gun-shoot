package com.gunshoot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SparksParticleEffect extends ParticleEffect {

	private static final int SPARK_PARTICLE_COUNT = 10;

	private final TextureRegion PARTICLE_REGION;

	public SparksParticleEffect(Vector2 emitPos, Texture shotgunTexture) {
		super(emitPos);

		PARTICLE_REGION = new TextureRegion(shotgunTexture, 7, 9, 2, 3);
	}

	@Override
	public void update(float dt) {
		for (Particle p : particles) {
			p.update(dt);
		}
	}

	@Override
	public void render(SpriteBatch b) {
		for (Particle p : particles) {
			p.render(b);
		}
	}

	@Override
	public void play() {
		super.play();

		for (int i = 0; i < SPARK_PARTICLE_COUNT; i++) {
			SparkParticle p = new SparkParticle(PARTICLE_REGION);

			p.generation = 0;

			p.position = emitPosition.cpy();
			p.direction = new Vector2(1f, 0f);

			p.speed = 500f * (float)Math.max(0.2f, Math.random());
			p.angularSpeed = 3000f * ((float)Math.random() - 0.5f);

			p.speedDecay = 0.02f;
			p.angularSpeedDecay = 0.04f;

			p.size = Math.max(0.2f, (float)Math.random() - 0.5f);
			p.sizeDecay = 0.03f * Math.max(0.2f, (float)Math.random());

			particles.add(p);
		}
	}
}
