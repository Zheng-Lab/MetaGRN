package birc.grni.gui;

import javax.swing.JFrame;

public class MetaScoreRank extends MetaScoreRankDisplay{

	public MetaScoreRank(JFrame frame, String[][] metaScoreRankTable)
	{
		super(frame, metaScoreRankTable);
		header.setTitle("MetaScore Rank");
		//header.setInfo("");
	}
}
