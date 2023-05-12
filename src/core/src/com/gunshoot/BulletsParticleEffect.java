package com.gunshoot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BulletsParticleEffect extends ParticleEffect {
	
	private static final int BULLET_COUNT = 5;
	
	private final TextureRegion PARTICLE_REGION;

	public BulletsParticleEffect(Vector2 emitPos, Texture shotgunTexture) {
		super(emitPos);
		PARTICLE_REGION = new TextureRegion(shotgunTexture, 9, 10, 14, 2);
	}

	@Override
	public void play() {
		super.play();
		
		for (int i = 0; i < BULLET_COUNT; i++) {
			BulletParticle p = new BulletParticle(PARTICLE_REGION);
			
			p.generation = 0;
			
			p.position = emitPosition.cpy();
			p.rotation = 0f;
			
			p.direction = new Vector2(1f, 0f);
			p.direction.rotateDeg((float)(Math.random() - 0.5) * 10f);

			p.size = 0.65f;
			p.speed = 1500f + (500f * (float)(Math.random() - 0.5));
			p.angularSpeed = 0f;
			p.rotationSpeed = 0f;
			
			p.speedDecay = 0f;
			p.angularSpeedDecay = 0f;
			p.rotationSpeedDecay = 0f;
			p.sizeDecay = 0f;
			
			particles.add(p);
		}
	}
}
