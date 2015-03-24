package birc.grni.rf;
class RtParam {
	private int nMin;
	private int varMin;
	private int savePred;
	private int bestFirst;
	private int rf;
	private int extraTrees;
	private long adjustDefaultK;
	private long extraTreesK;
	private int maxnbsplits=-1;
	
	public void setMaxnbsplits(int maxnbsplits){
		this.maxnbsplits=maxnbsplits;
	}
	public int getMaxnbsplits(){
		return this.maxnbsplits;
	}
	public int getnMin() {
		return nMin;
	}
	public void setnMin(int nMin) {
		this.nMin = nMin;
	}
	public int getVarMin() {
		return varMin;
	}
	public void setVarMin(int varMin) {
		this.varMin = varMin;
	}
	public int getSavePred() {
		return savePred;
	}
	public void setSavePred(int savePred) {
		this.savePred = savePred;
	}
	public int getBestFirst() {
		return bestFirst;
	}
	public void setBestFirst(int bestFirst) {
		this.bestFirst = bestFirst;
	}
	public int getRf() {
		return rf;
	}
	public void setRf(int rf) {
		this.rf = rf;
	}
	public int getExtraTrees() {
		return extraTrees;
	}
	public void setExtraTrees(int extraTrees) {
		this.extraTrees = extraTrees;
	}
	public long getAdjustDefaultK() {
		return adjustDefaultK;
	}
	public void setAdjustDefaultK(long adjustDefaultK) {
		this.adjustDefaultK = adjustDefaultK;
	}
	public long getExtraTreesK() {
		return extraTreesK;
	}
	public void setExtraTreesK(long extraTreesK) {
		this.extraTreesK = extraTreesK;
	}
}
