package jx.vein.javajar;

public class JXFVJavaInterface {
	public static final int veinIDLength = 50;
	public static final int groupIDLength = 20;
	
	public static final int imgRows = 362;
	public static final int imgCols = 460;
	
	public static final int veinImgSize = imgRows * imgCols;
	public static final int veinSampleSize = 166572;
	public static final int veinFeatureSize =1344;
	
	static {
		System.loadLibrary("sqlite3");
		System.loadLibrary("usb1.0");
		System.loadLibrary("JXFVBase");
		System.loadLibrary("JXFVD");
		System.loadLibrary("JXFVM");
		System.loadLibrary("JXFVJavaInterface");
	}

	private native long InitUSBDriver();
	private native void DeInitUSBDriver(long devHandle);
	private native int IsFVDConnected(long devHandle);
	private native int ConnFVD(long devHandle);
	private native void DisConnFVD(long devHandle);
	private native int IsFingerDetected(long devHandle);
	private native int IsFingerTouched(long devHandle);
	private native int InitCapEnv(long devHandle);
	private native int DeInitCapEnv(long devHandle);
	private native int IsVeinImgOK(long devHandle, byte[] img_buf);
	private native void LoadVeinSample(long devHandle, byte[] sample_buf);
	private native int SetBellRing(long devHandle, int ms);
	private native int IsFVDWorking(long devHandle);
	private native int SetRecgResult(long devHandle, int state);

	private native int CheckVeinSampleQuality(byte[] sample_buf);
	private native int GrabVeinFeature(byte[] sample_buf, byte[] feat_buf);
	private native int VericateTwoVeinFeature(byte[] feat_buf1, byte[] feat_buf2);

	private native int IsVeinDBExist(String filename);
	private native int CreateVeinDatabase(String filename);
	private native long InitVeinDatabase(String filename);
	private native void DeInitVeinDatabase(long dbHandle);
	private native int CountAllFeatures(long dbHandle);
	private native int CountGroupFeatures(long dbHandle, byte[] groupID);
	private native int IsFeatureDuplicate(long dbHandle, byte[] feat_buf);
	private native int IsFeatureDuplicateInGroup(long dbHandle, byte[] feat_buf, byte[] groupID);
	private native int AddOneVeinFeature(long dbHandle, byte[] feat_buf, byte[] veinID, byte[] groupID);
	private native int AddTwoVeinFeature(long dbHandle, byte[] feat_buf1, byte[] feat_buf2, byte[] veinID, byte[] groupID);
	private native int AddThreeVeinFeature(long dbHandle, byte[] feat_buf1, byte[] feat_buf2, byte[] feat_buf3, byte[] veinID, byte[] groupID);
	private native int RecognizeVeinFeature(long dbHandle, byte[] feat_buf,  byte[] veinIDs);
	private native int RecognizeVeinFeatureInGroup(long dbHandle, byte[] feat_buf, byte[] groupID, byte[] veinIDs);
	private native int RemoveVeinFeature(long dbHandle, byte[] veinID, byte[] groupID);
	private native int RemoveGroupVeinFeature(long dbHandle, byte[] groupID);
	private native int ChangeVeinGroup(long dbHandle, byte[] veinID, byte[] groupID);

	public long jxInitUSBDriver()
	{
		return InitUSBDriver();
	}

	public void jxDeInitUSBDriver(long devHandle)
	{
		DeInitUSBDriver(devHandle);
	}

	public int jxIsFVDConnected(long devHandle)
	{
		int ret = IsFVDConnected(devHandle);
		return ret;
	}
	
	public int jxConnFVD(long devHandle)
	{
		int ret = ConnFVD(devHandle);
		return ret;
	}

	public void jxDisConnFVD(long devHandle)
	{
		DisConnFVD(devHandle);
	}
	
	public int jxIsFingerDetected(long devHandle)
	{
		int ret = IsFingerDetected(devHandle);
		return ret;
	}
	
	public int jxIsFingerTouched(long devHandle)
	{
		int ret = IsFingerTouched(devHandle);
		return ret;
	}
	
	public int jxInitCapEnv(long devHandle)
	{
		int ret = InitCapEnv(devHandle);
		return ret;
	}
	
	public int jxDeInitCapEnv(long devHandle)
	{
		int ret = DeInitCapEnv(devHandle);
		return ret;
	}
	
	public int jxIsVeinImgOK(long devHandle, byte[] img_buf) throws Exception
	{
		if(img_buf.length != veinImgSize)
			throw new Exception("Invalid buf length");
		
		int ret = IsVeinImgOK(devHandle, img_buf);
		return ret;
	}

	public void jxLoadVeinSample(long devHandle, byte[] sample_buf) throws Exception
	{
		if(sample_buf.length != veinSampleSize)
			throw new Exception("Invalid buf length");
		LoadVeinSample(devHandle, sample_buf);
	}

	public int jxSetBellRing(long devHandle, int ms)
	{
		int ret = SetBellRing(devHandle, ms);
		return ret;
	}
	
	public int jxIsFVDWorking(long devHandle)
	{
		int ret = IsFVDWorking(devHandle);
		return ret;
	}
	
	public int jxSetRecgResult(long devHandle, int state)
	{
		int ret = SetRecgResult(devHandle, state);
		return ret;
	}
	
	
	public int jxCheckVeinSampleQuality(byte[] sample_buf) throws Exception
	{
		if(sample_buf.length != veinSampleSize)
			throw new Exception("Invalid buf length");
		int ret = CheckVeinSampleQuality(sample_buf);
		return ret;
	}
	
	public int jxGrabVeinFeature(byte[] sample_buf, byte[] feat_buf) throws Exception
	{
		if(sample_buf.length != veinSampleSize || feat_buf.length != veinFeatureSize)
			throw new Exception("Invalid buf length");
		int ret = GrabVeinFeature(sample_buf, feat_buf);
		return ret;
	}
	
	public int jxVericateTwoVeinFeature(byte[] feat_buf1, byte[] feat_buf2) throws Exception
	{
		if(feat_buf1.length != veinFeatureSize || feat_buf2.length != veinFeatureSize)
			throw new Exception("Invalid buf length");
		int ret = VericateTwoVeinFeature(feat_buf1, feat_buf2);
		return ret;
	}
	
	
	public int jxIsVeinDBExist(String filename)
	{
		int ret = IsVeinDBExist(filename);
		return ret;
	}
	
	public int jxCreateVeinDatabase(String filename)
	{
		int ret = CreateVeinDatabase(filename);
		return ret;
	}
	
	public long jxInitVeinDatabase(String filename)
	{
		long ret = InitVeinDatabase(filename);
		return ret;
	}
	
	public void jxDeInitVeinDatabase(long dbHandle)
	{
		DeInitVeinDatabase(dbHandle);
	}
	
	public int jxCountAllFeatures(long dbHandle)
	{
		int ret = CountAllFeatures(dbHandle);
		return ret;
	}
	
	public int jxCountGroupFeatures(long dbHandle, byte[] groupID) throws Exception
	{
		if(groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = CountGroupFeatures(dbHandle, groupID);
		return ret;
	}
	
	public int jxIsFeatureDuplicate(long dbHandle, byte[] feat_buf) throws Exception
	{
		if(feat_buf.length != veinFeatureSize)
			throw new Exception("Invalid buf length");
		int ret = IsFeatureDuplicate(dbHandle, feat_buf);
		return ret;
	}
	
	public int jxIsFeatureDuplicateInGroup(long dbHandle, byte[] feat_buf, byte[] groupID) throws Exception
	{
		if(feat_buf.length != veinFeatureSize || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = IsFeatureDuplicateInGroup(dbHandle, feat_buf, groupID);
		return ret;
	}
	
	public int jxAddOneVeinFeature(long dbHandle, byte[] feat_buf, byte[] veinID, byte[] groupID) throws Exception
	{
		if(feat_buf.length != veinFeatureSize || veinID.length != veinIDLength || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = AddOneVeinFeature(dbHandle, feat_buf, veinID, groupID);
		return ret;
	}
	
	public int jxAddTwoVeinFeature(long dbHandle, byte[] feat_buf1, byte[] feat_buf2, byte[] veinID, byte[] groupID) throws Exception
	{
		if(feat_buf1.length != veinFeatureSize || feat_buf2.length != veinFeatureSize)
			throw new Exception("Invalid buf length");
		if(veinID.length != veinIDLength || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = AddTwoVeinFeature(dbHandle, feat_buf1, feat_buf2, veinID, groupID);
		return ret;
	}
	
	
	
	public int jxAddThreeVeinFeature(long dbHandle, byte[] feat_buf1, byte[] feat_buf2, byte[] feat_buf3, byte[] veinID, byte[] groupID) throws Exception
	{
		if(feat_buf1.length != veinFeatureSize || feat_buf2.length != veinFeatureSize || feat_buf3.length != veinFeatureSize)
			throw new Exception("Invalid buf length");
		if(veinID.length != veinIDLength || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = AddThreeVeinFeature(dbHandle, feat_buf1, feat_buf2, feat_buf3, veinID, groupID);
		return ret;
	}
	
	
	public int jxRecognizeVeinFeature(long dbHandle, byte[] feat_buf,  byte[] veinIDs) throws Exception
	{
		if(feat_buf.length != veinFeatureSize || veinIDs.length != 5 * veinIDLength)
			throw new Exception("Invalid buf length");
		int ret = RecognizeVeinFeature(dbHandle, feat_buf, veinIDs);
		return ret;
	}
	
	public int jxRecognizeVeinFeatureInGroup(long dbHandle, byte[] feat_buf, byte[] groupID, byte[] veinIDs) throws Exception
	{
		if(feat_buf.length != veinFeatureSize || groupID.length != groupIDLength || veinIDs.length != 5 * veinIDLength)
			throw new Exception("Invalid buf length");
		int ret = RecognizeVeinFeatureInGroup(dbHandle, feat_buf, groupID, veinIDs);
		return ret;
	}
	
	public int jxRemoveVeinFeature(long dbHandle, byte[] veinID, byte[] groupID) throws Exception
	{
		if(veinID.length != veinIDLength || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = RemoveVeinFeature(dbHandle, veinID, groupID);
		return ret;
	}
	
	public int jxRemoveGroupVeinFeature(long dbHandle, byte[] groupID) throws Exception
	{
		if(groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = RemoveGroupVeinFeature(dbHandle, groupID);
		return ret;
	}
	
	public int jxChangeVeinGroup(long dbHandle, byte[] veinID, byte[] groupID) throws Exception
	{
		if(veinID.length != veinIDLength || groupID.length != groupIDLength)
			throw new Exception("Invalid buf length");
		int ret = ChangeVeinGroup(dbHandle, veinID, groupID);
		return ret;
	}
}
