package com.maple.mstd.game;

/**
 * 存储敌人移动路径坐标
 */
public class Path {

    private final float[] mCoordinatesX;
    private final float[] mCoordinatesY;

    private int mIndex;
    private boolean mLengthChanged = false;
    private float mLength;

    /**
     * pLength为路径节点
     *
     * @param pLength
     */
    public Path(final int pLength) {
        this.mCoordinatesX = new float[pLength];
        this.mCoordinatesY = new float[pLength];

        this.mIndex = 0;
        this.mLengthChanged = false;
    }

//	public Path(final float[] pCoordinatesX, final float[] pCoordinatesY) throws IllegalArgumentException {
//		if (pCoordinatesX.length != pCoordinatesY.length) {
//			throw new IllegalArgumentException("Coordinate-Arrays must have the same length.");
//		}
//
//		this.mCoordinatesX = pCoordinatesX;
//		this.mCoordinatesY = pCoordinatesY;
//
//		this.mIndex = pCoordinatesX.length;
//		this.mLengthChanged = true;
//	}
//	public Path(final Path pPath) {
//		final int size = pPath.getSize();
//		this.mCoordinatesX = new float[size];
//		this.mCoordinatesY = new float[size];
//
//		System.arraycopy(pPath.mCoordinatesX, 0, this.mCoordinatesX, 0, size);
//		System.arraycopy(pPath.mCoordinatesY, 0, this.mCoordinatesY, 0, size);
//
//		this.mIndex = pPath.mIndex;
//		this.mLengthChanged = pPath.mLengthChanged;
//		this.mLength = pPath.mLength;
//	}
//
//	public Path clone() {
//		return new Path(this);
//	}

    public Path to(final float pX, final float pY) {
        this.mCoordinatesX[this.mIndex] = pX;
        this.mCoordinatesY[this.mIndex] = pY;
        this.mIndex++;
        this.mLengthChanged = true;
        return this;
    }

    public float[] getCoordinatesX() {
        return this.mCoordinatesX;
    }

    public float[] getCoordinatesY() {
        return this.mCoordinatesY;
    }

    public int getSize() {
        return this.mCoordinatesX.length;
    }

//	public float getLength() {
//		if (this.mLengthChanged) {
//			this.updateLength();
//		}
//		return this.mLength;
//	}
//
//	public float getSegmentLength(final int pSegmentIndex) {
//		final float[] coordinatesX = this.mCoordinatesX;
//		final float[] coordinatesY = this.mCoordinatesY;
//
//		final int nextSegmentIndex = pSegmentIndex + 1;
//
//		final float dx = coordinatesX[pSegmentIndex] - coordinatesX[nextSegmentIndex];
//		final float dy = coordinatesY[pSegmentIndex] - coordinatesY[nextSegmentIndex];
//
//		return FloatMath.sqrt(dx * dx + dy * dy);
//	}
//
//	private void updateLength() {
//		float length = 0.0f;
//
//		for (int i = this.mIndex - 2; i >= 0; i--) {
//			length += this.getSegmentLength(i);
//		}
//		this.mLength = length;
//	}
}
