package com.gunshoot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Shotgun extends Gun {

	private final Vector2 HANDLE_POS = new Vector2(15f, 0f);
	private final Vector2 FIRE_POS = new Vector2(35.0f, 3.5f);

	float bodyPosXSpring = 0f;

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
		bullets = new BulletsParticleEffect(fireEmitPos);

		handle.position
			.add(body.position)
			.add(HANDLE_POS);
	}

	@Override
	public void update(float dt) {

		bodyPosXSpring = spring(body.position.x, Game.camera.viewportWidth / 2f - 50f, 5f, 1f, bodyPosXSpring);
		body.position.x += bodyPosXSpring;

		body.position.y = MathUtils.lerp(body.position.y, Game.camera.viewportHeight / 2f, 20f * dt);
		body.rotation   = MathUtils.lerp(body.rotation, 0f, 20f * dt);

		Matrix3 mat = new Matrix3()
			.setToTranslation(body.position)
			.rotate(body.rotation)
			.translate(HANDLE_POS);

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
	public void shoot() {
		body.position.x -= 8f * Math.random();
		body.position.y -= 5f * (Math.random() - 0.5f);
		body.rotation -= 23f * Math.random();
		sparks.play();
		bullets.play();
		Game.shakeCamera();
	}

	@Override
	public void dispose() {
		shotgunTexture.dispose();
	}

	private float spring(float from, float to, float bounciness, float tension, float intermediate) {
		return MathUtils.lerp(intermediate, (to - from) * tension, 1f / bounciness);
	}
}
