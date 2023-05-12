package com.gunshoot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Shotgun extends Gun {

	private final Vector2 HANDLE_POS = new Vector2(20f, 0f);
	private final Vector2 FIRE_POS = new Vector2(35.0f, 3.5f);
	private final float PUMP_DO = 0.43f;
	private final float PUMP_TGT_OFFSET = -15f;
	private final float PUMP_FINISH = 0.3f;
	
	private float bodyPosXSpring = 0f;
	private float bodyRotSpring = 0f;
	private float pumpStartTimer = 0f;
	private boolean tickingPumpTimer = false;
	private float handlePosX = 0f;
	private float handlePosXSpring = 0f;
	private int pumpStage = 0;
	private boolean pumping = false;
	private boolean canFire = true;
	
	private Texture shotgunTexture;
	private Component body;
	private Component handle;

	private SparksParticleEffect sparks;
	private BulletsParticleEffect bullets;

	public Shotgun() {
		super();

		shotgunTexture = new Texture("shotgun.png");

		body   = new Component(new TextureRegion(shotgunTexture, 0, 0, 35, 9));
		handle = new Component(new TextureRegion(shotgunTexture, 0, 9, 7, 3));

		body.sprite.setOrigin(5f, body.sprite.getHeight() - 4f);
		body.position.set(Game.camera.viewportWidth / 2f - 50f, Game.camera.viewportHeight / 2f);

		handle.sprite.setOrigin(0f, handle.sprite.getHeight());

		Vector2 fireEmitPos = FIRE_POS
			.cpy()
			.add(body.position)
			.sub(body.sprite.getOriginX(), body.sprite.getOriginY());

		sparks  = new SparksParticleEffect (fireEmitPos, shotgunTexture);
		bullets = new BulletsParticleEffect(fireEmitPos, shotgunTexture);

		handle.position
			.add(body.position)
			.add(HANDLE_POS);
	}

	@Override
	public void update(float dt) {
		
		if (tickingPumpTimer) {
			pumpStartTimer += dt;
		}
		
		if (pumping) {
			pumpUpdate(dt);
			if (pumpStartTimer > PUMP_FINISH) {
				pumping = false;
				pumpStartTimer = 0f;
				pumpStage = 0;
				canFire = true;
			}
		} else {
			if (pumpStartTimer > PUMP_DO) {
				pumping = true;
				pumpStartTimer = 0f;
				tickingPumpTimer = false;
			}
		}

		bodyPosXSpring = spring(body.position.x, Game.camera.viewportWidth / 2f - 50f, 8f, 0.5f, bodyPosXSpring);
		body.position.x += bodyPosXSpring;
		
		bodyRotSpring = spring(body.rotation, 0f, 10f, 0.5f, bodyRotSpring);
		body.rotation += bodyRotSpring;

		body.position.y = MathUtils.lerp(body.position.y, Game.camera.viewportHeight / 2f, 20f * dt);

		Matrix3 mat = new Matrix3()
			.setToTranslation(body.position)
			.rotate(body.rotation)
			.translate(new Vector2(HANDLE_POS.x + handlePosX, HANDLE_POS.y));

		mat.getTranslation(handle.position);
		handle.rotation = mat.getRotation();

		sparks.update(dt);
		bullets.update(dt);
	}

	@Override
	public void render(SpriteBatch b) {
		sparks.render(b);
		bullets.render(b);
		body.render(b);
		handle.render(b);
	}
	
	@Override
	public void renderBloom(SpriteBatch b) {
		sparks.render(b);
		bullets.render(b);
	}
	
	@Override
	public boolean shoot() {
		if (!canFire) {
			return false;
		} else {
			canFire = false;
		}
		body.position.x -= 14f + 7f * Math.random();
		body.position.y -= 5f * (Math.random() - 0.5f);
		body.rotation -= 15 + 20f * Math.random();
		sparks.play();
		bullets.play();
		tickingPumpTimer = true;
		return true;
	}

	@Override
	public void dispose() {
		shotgunTexture.dispose();
	}

	private void pumpUpdate(float dt) {
		float pumpBounce = 8f;
		float pumpTension = 0.9f;
		if (pumpStage == 0) {
			handlePosXSpring = spring(handlePosX, PUMP_TGT_OFFSET, pumpBounce, pumpTension, handlePosXSpring);
			handlePosX += handlePosXSpring;
			if (handlePosX > PUMP_TGT_OFFSET) {
				pumpStage = 1;
				body.position.x -= 7f;
			}
		} else if (pumpStage == 1) {
			handlePosXSpring = spring(handlePosX, 0f, pumpBounce, pumpTension, handlePosXSpring);
			handlePosX += handlePosXSpring;
			pumpStartTimer += dt;
		}
	}
	
	private float spring(float from, float to, float bounciness, float tension, float intermediate) {
		return MathUtils.lerp(intermediate, (to - from) * tension, 1f / bounciness);
	}
}
