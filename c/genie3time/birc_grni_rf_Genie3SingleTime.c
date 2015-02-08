#include "birc_grni_rf_Genie3SingleTime.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdarg.h>

#include "tree-model.h"
#include "tree-model.c"
#include "f-table.c"
#include "tree-multiregr.c"

CORETABLE_TYPE *core_table_y;

int *row_nodenumber;
int *column_nodenumber;

int *row_testattribute;
int *column_testattribute;

int *row_testthreshold;
int *column_testthreshold;

int *row_children;
int *column_children;

int *row_ls;
int *column_ls;

int *row_objectweights;
int *column_objectweights;

int *row_weight;
int *column_weight;

int *row_savepred;
int *column_savepred;

int *row_indexprediction;
int *column_indexprediction;

int *row_predictions;
int *column_predictions;

int *row_nodesize;
int *column_nodesize;

int row_ls_TreeEnsembleStruct;
int column_ls_TreeEnsembleStruct;

typedef struct {
    int **nodenumber;	// 1*1
	int **testattribute;
	float **testthreshold;
	int **children;	// two column
	int **ls;
	float **objectweights;
	float **weight;	// 1*1
	int **savepred;	// 1*1
	int **indexprediction;
	float **predictions;
	float **nodesize;
} TreeStruct;

typedef struct {
	int **ls;
    TreeStruct **trees;
} TreeEnsembleStruct;

float getobjy_multiregr_learn_matlab(int obj, int att)
{
	return (float)core_table_y[goal_multiregr[att]*nb_obj_in_core_table+object_mapping[obj]];
}

void set_tree_param(JNIEnv * env, jobject treeparam) {
	int nbfield,ifield;
	int nmin=1, bestfirst=0, extratrees=0, adjustdefaultk=1, extratreesk=1, maxnbsplits=5, savepred=1, rf=0;
	float varmin=0.0;
	float a_r=1.0;
	
	//char *fname;
	
	jclass class_RtParam;
	jfieldID id_nmin, id_varmin, id_bestfirst, id_extratrees, id_rf, id_adjustdefaultk, id_savepred, id_extratreesk, id_maxnbsplits;
  
	//TODO:
	/*
	if (!mxIsStruct(treeparam))
		mexErrMsgTxt("Fourth argument must be a structure\n");
	*/
	
	//nbfield=mxGetNumberOfFields(treeparam);
  
	/*
	for (ifield=0;ifield<nbfield;ifield++) {
		fname=(char *)mxGetFieldNameByNumber(treeparam,ifield);
		if (strcmp(fname,"nmin")==0) {
			nmin=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"varmin")==0) {
			varmin=(float)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"bestfirst")==0) {
			bestfirst=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"maxnbsplits")==0) {
			maxnbsplits=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"extratrees")==0) {
			extratrees=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"extratreesk")==0) {
			extratreesk=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"rf")==0) {
			rf=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"adjustdefaultk")==0) {
			adjustdefaultk=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else if (strcmp(fname,"savepred")==0) {      
			savepred=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
		} else
			mexPrintf("Field %s non recognized\n",fname);
	}
	*/
	
	class_RtParam = (*env)->GetObjectClass(env, treeparam);
	id_nmin = (*env)->GetFieldID(env, class_RtParam, "nMin", "I");
	nmin = (*env)->GetIntField(env, treeparam, id_nmin);
	
	id_varmin = (*env)->GetFieldID(env, class_RtParam, "varMin", "I");
	varmin = (*env)->GetIntField(env, treeparam, id_varmin);
  
	id_bestfirst = (*env)->GetFieldID(env, class_RtParam, "bestFirst", "I");
	bestfirst = (*env)->GetIntField(env, treeparam, id_bestfirst);
	
	id_maxnbsplits = (*env)->GetFieldID(env,class_RtParam,"maxnbsplits","I");
	if((*env)->GetIntField(env,treeparam,id_maxnbsplits)!=-1) {
		maxnbsplits=(*env)->GetIntField(env,treeparam,id_maxnbsplits);
	}
	
	id_extratrees = (*env)->GetFieldID(env, class_RtParam, "extraTrees", "I");
	extratrees = (*env)->GetIntField(env, treeparam, id_extratrees);
	
	id_rf = (*env)->GetFieldID(env, class_RtParam, "rf", "I");
	rf = (*env)->GetIntField(env, treeparam, id_rf);
	
	id_adjustdefaultk = (*env)->GetFieldID(env, class_RtParam, "adjustDefaultK", "J");
	adjustdefaultk = (int)((*env)->GetLongField(env, treeparam, id_adjustdefaultk));
	
	id_savepred = (*env)->GetFieldID(env, class_RtParam, "savePred", "I");
	savepred = (*env)->GetIntField(env, treeparam, id_savepred);
		
	init_multiregr_trees(nmin,varmin,a_r,savepred);
  
	set_test_classical();
  
	set_best_first(bestfirst,0,maxnbsplits);

	if (adjustdefaultk==1)
		extratreesk=ceil(sqrt(nb_attributes));
	else {
		id_extratreesk = (*env)->GetFieldID(env, class_RtParam, "extraTreesK", "J");
		extratreesk = (int)((*env)->GetLongField(env, treeparam, id_extratreesk));
	}
  
	if (extratrees==1) {
		find_a_threshold_num=find_a_threshold_at_random_multiregr;
		find_a_threshold_symb=find_the_best_threshold_symb_multiregr;
	} else {
		find_a_threshold_num=find_the_best_threshold_multiregr;
		find_a_threshold_symb=find_the_best_threshold_symb_multiregr;
	}
  
	if (extratrees==1) {
		find_a_split=find_a_split_at_random_et;
		nb_of_random_tests=extratreesk;
		random_split_score_threshold=10.0;
		rf_k=extratreesk;
	} else if (rf==1) {
		find_a_split=find_the_best_split_among_k;
		nb_of_random_tests=extratreesk;
		random_split_score_threshold=10.0;
		rf_k=extratreesk;
	} else {
		find_a_split=find_the_best_split;
		random_split_score_threshold=10.0;
	}
}

int mart=0;

void set_ensemble_param(JNIEnv * env, jobject treeparam) {
	int nbfield, ifield, method;
	int nbterms=1;
	int bootstrap=0;
	float martmu=1.0;
	//char *fname;
	
	jclass class_RtEnsParam;
	jfieldID id_nbterms,id_bootstrap,id_rtparam,id_mart,id_martmu;
	jobject rtparam;

	mart=0;

	//TODO:
	/*
	if (!mxIsStruct(treeparam))
		mexErrMsgTxt("Fourth argument must be a structure\n");
	*/

	//nbfield=mxGetNumberOfFields(treeparam);
  
	/*for (ifield=0;ifield<nbfield;ifield++) {
		fname=(char *)mxGetFieldNameByNumber(treeparam,ifield);
    if (strcmp(fname,"nbterms")==0) {
		nbterms=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
    } else if (strcmp(fname,"bootstrap")==0) {
		bootstrap=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
    } else if (strcmp(fname,"rtparam")==0) {
		set_tree_param(mxGetFieldByNumber(treeparam,0,ifield));
    } else if (strcmp(fname,"mart")==0) {      
		mart=(int)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
    } else if (strcmp(fname,"martmu")==0) {      
		martmu=(float)mxGetScalar(mxGetFieldByNumber(treeparam,0,ifield));
    } else
		mexPrintf("Field %s non recognized\n",fname);*/
	
	class_RtEnsParam = (*env)->GetObjectClass(env, treeparam);
	id_nbterms = (*env)->GetFieldID(env, class_RtEnsParam, "nbTerms", "I");
	nbterms = (*env)->GetIntField(env, treeparam, id_nbterms);

	id_bootstrap = (*env)->GetFieldID(env, class_RtEnsParam, "bootStrap", "I");
	bootstrap = (*env)->GetIntField(env, treeparam, id_bootstrap);

	id_rtparam = (*env)->GetFieldID(env, class_RtEnsParam, "rtParam", "Lbirc/grni/rf/RtParam;");
	rtparam = (*env)->GetObjectField(env, treeparam, id_rtparam);
	set_tree_param(env, rtparam);
	
	id_mart = (*env)->GetFieldID(env,class_RtEnsParam,"mart","I");
	if((*env)->GetIntField(env,treeparam,id_mart)!=-1)
		mart=(*env)->GetIntField(env,treeparam,id_mart);
	
	id_martmu=(*env)->GetFieldID(env,class_RtEnsParam,"martmu","F");
	if((*env)->GetFloatField(env,treeparam,id_martmu)!=-1.0f)
		martmu=(*env)->GetFloatField(env,treeparam,id_martmu);
	
	//TODO:
	/*
    else
		mexPrintf("Field %s non recognized\n",fname);
	*/ 

	if (bootstrap == 1)
		method=1;
	else if (mart == 1)
		method=2;
	else
		method=0;
	
	set_ensemble_method_parameters(method,nbterms,1,0,martmu);
}

int save_ensemble_ls_pos_2=0;
int pos_prediction_values=0;
float sum_weight=0.0;

void create_struct_from_tree(int t, TreeStruct **structvector) {
	int *tmp_store;
	int **tmp;
	
	int *tmp1_store;
	int **tmp1;
	
	float *tmp2_store;
	float **tmp2;
	
	int *tmp3_store;
	int **tmp3;
	
	int *tmp4_store;
	int **tmp4;
	
	float *tmp5_store;
	float **tmp5;
	
	float *tmp6_store;
	float **tmp6;
	
	int *tmp7_store;
	int **tmp7;
	
	int *tmp8_store;
	int **tmp8;
	
	float *tmp9_store;
	float **tmp9;
	
	float *tmp10_store;
	float **tmp10;
	
	int nodenumber, start, end, i, j, nb_pred;

	/* NODENUMBER */
	
	start=ltrees[t];
	
	if (t==current_nb_of_ensemble_terms-1) {
		nodenumber=index_nodes-ltrees[t]+1;
		end=index_nodes+1;
	}else {
		nodenumber=ltrees[t+1]-ltrees[t];
		end=ltrees[t+1];
	}

	//tmp=mxCreateNumericMatrix(1,1,mxINT32_CLASS,mxREAL);
	//*((int *)mxGetPr(tmp))=nodenumber;
	//mxSetFieldByNumber(structvector,t,0,tmp);
	tmp_store = (int *)malloc(1*1*sizeof(int));
	tmp = (int **)malloc(1*sizeof(int *));
	for(i=0; i<1; i++)
		tmp[i] = &tmp_store[i*1];
	tmp[0][0] = nodenumber;
	structvector[t][0].nodenumber = tmp;
	row_nodenumber[t] = 1;
	column_nodenumber[t] = 1;
  
	/* TESTATTRIBUTE */

	//tmp=mxCreateNumericMatrix(nodenumber,1,mxINT32_CLASS,mxREAL);
	//ptr_int=(int *)mxGetPr(tmp);
	//for (i=start; i<end;i++,ptr_int++)
	//	*ptr_int=tested_attribute[i]+1;
	//mxSetFieldByNumber(structvector,t,1,tmp);
	tmp1_store = (int *)malloc(nodenumber*1*sizeof(int));
	tmp1 = (int **)malloc(nodenumber*sizeof(int *));
	for(i=0;i<nodenumber;i++)
		tmp1[i] = &tmp1_store[i*1];
	for(i=start; i<end; i++, tmp1_store++)
		*tmp1_store=tested_attribute[i]+1;
	structvector[t][0].testattribute=tmp1;
	row_testattribute[t] = nodenumber;
	column_testattribute[t] = 1;

	/* TESTTHRESHOLD */

	//tmp=mxCreateNumericMatrix(nodenumber,1,mxSINGLE_CLASS,mxREAL);
	//ptr_float=(float *)mxGetPr(tmp);
	//for (i=start; i<end;i++,ptr_float++)
	//	*ptr_float=threshold[i].f;
	//mxSetFieldByNumber(structvector,t,2,tmp);
	tmp2_store=(float *)malloc(nodenumber*1*sizeof(float));
	tmp2 = (float **)malloc(nodenumber*sizeof(float *));
	for(i=0; i< nodenumber; i++)
		tmp2[i] = &tmp2_store[i*1];
	for (i=start; i<end; i++,tmp2_store++)
		*tmp2_store=threshold[i].f;
	structvector[t][0].testthreshold=tmp2;
	row_testthreshold[t] = nodenumber;
	column_testthreshold[t] = 1;
  
	/* CHILDREN */

	//tmp=mxCreateNumericMatrix(nodenumber,2,mxINT32_CLASS,mxREAL);
	//ptr_int=(int *)mxGetPr(tmp);
	//for (i=start; i<end;i++,ptr_int++)
	//	*ptr_int=i+left_successor[i]-start+1;
	//for (i=start; i<end;i++,ptr_int++)
	//	*ptr_int=i+right_successor[i]-start+1;
	//mxSetFieldByNumber(structvector,t,3,tmp);
	tmp3_store = (int *)malloc(nodenumber*2*sizeof(int));
	tmp3 = (int **)malloc(nodenumber*sizeof(int *));
	for(i=0;i<nodenumber;i++)
		tmp3[i] = &tmp3_store[i*2];
	for (i=start; i<end;i++,tmp3_store++)
		*tmp3_store=i+left_successor[i]-start+1;
	for (i=start; i<end;i++,tmp3_store++)
		*tmp3_store=i+right_successor[i]-start+1;
	structvector[t][0].children = tmp3;
	row_children[t] = nodenumber;
	column_children[t] = 2;
  
	/* LS, OBJECTWEIGHTS */
	/*tmp=mxCreateNumericMatrix(save_ensemble_ls_size[t],1,mxINT32_CLASS,mxREAL);
	tmp2=mxCreateNumericMatrix(save_ensemble_ls_size[t],1,mxSINGLE_CLASS,mxREAL);
	ptr_int=(int *)mxGetPr(tmp);
	ptr_float=(float *)mxGetPr(tmp2);
	for (i=0; i<save_ensemble_ls_size[t];i++,ptr_int++,ptr_float++) {
		*ptr_int=save_ensemble_ls_vector[save_ensemble_ls_pos_2]+1;
		*ptr_float=save_ensemble_ls_weight[save_ensemble_ls_pos_2];
		save_ensemble_ls_pos_2++;
	}
	mxSetFieldByNumber(structvector,t,4,tmp);
	mxSetFieldByNumber(structvector,t,5,tmp2);*/
	
	tmp4_store = (int *)malloc(save_ensemble_ls_size[t] * 1 * sizeof(int));
	tmp4 = (int **)malloc(save_ensemble_ls_size[t] * sizeof(int *));
	for(i=0;i<save_ensemble_ls_size[t];i++)
		tmp4[i] = &tmp4_store[i*1];
	tmp5_store = (float *)malloc(save_ensemble_ls_size[t] * 1 * sizeof(float));
	tmp5 = (float **)malloc(save_ensemble_ls_size[t] * sizeof(float *));
	for(i=0;i<save_ensemble_ls_size[t];i++)
		tmp5[i] = &tmp5_store[i*1];
	for(i=0; i<save_ensemble_ls_size[t];i++,tmp4_store++,tmp5_store++) {
		*tmp4_store=save_ensemble_ls_vector[save_ensemble_ls_pos_2]+1;
		*tmp5_store=save_ensemble_ls_weight[save_ensemble_ls_pos_2];
		save_ensemble_ls_pos_2++;
	}
	structvector[t][0].ls = tmp4;
	structvector[t][0].objectweights = tmp5;
	row_ls[t] = save_ensemble_ls_size[t];
	column_ls[t] = 1;
	row_objectweights[t] = save_ensemble_ls_size[t];
	column_objectweights[t] = 1;

	/* WEIGHT */
	/*tmp=mxCreateNumericMatrix(1,1,mxSINGLE_CLASS,mxREAL);
	if (average_predictions_ltrees)
		*((float *)mxGetPr(tmp))=ltrees_weight[t]/sum_weight;
	else
		*((float *)mxGetPr(tmp))=ltrees_weight[t];
	mxSetFieldByNumber(structvector,t,6,tmp);*/
	
	tmp6_store = (float *)malloc(1*1*sizeof(float));
	tmp6 = (float **)malloc(1*sizeof(float *));
	for(i=0;i<1;i++)
		tmp6[i] = &tmp6_store[i*1];
	if (average_predictions_ltrees)
		tmp6[0][0]=ltrees_weight[t]/sum_weight;
	else
		tmp6[0][0]=ltrees_weight[t];
	structvector[t][0].weight = tmp6;
	row_weight[t] = 1;
	column_weight[t] = 1;

	/* SAVEPRED */
	/*tmp=mxCreateNumericMatrix(1,1,mxINT32_CLASS,mxREAL);
	*((int *)mxGetPr(tmp))=multiregr_savepred;
	mxSetFieldByNumber(structvector,t,7,tmp);*/
	tmp7_store = (int *)malloc(1*1*sizeof(int));
	tmp7 = (int **)malloc(1*sizeof(int *));
	for(i=0;i<1;i++)
		tmp7[i] = &tmp7_store[i*1];
	tmp7[0][0] = multiregr_savepred;
	structvector[t][0].savepred = tmp7;
	row_savepred[t] = 1;
	column_savepred[t] = 1;
	
	if (multiregr_savepred) {
		/* INDEXPREDICTION */
		/*tmp=mxCreateNumericMatrix(nodenumber,1,mxINT32_CLASS,mxREAL);
		ptr_int=(int *)mxGetPr(tmp);
		for (i=start; i<end;i++,ptr_int++) {
			if (prediction[i]<0)
				*ptr_int=0;
			else
				*ptr_int=prediction[i]-pos_prediction_values+1;
		}
		mxSetFieldByNumber(structvector,t,8,tmp);*/
		tmp8_store = (int *)malloc(nodenumber*1*sizeof(int));
		tmp8 = (int **)malloc(nodenumber*sizeof(int *));
		for(i=0;i<nodenumber;i++)
			tmp8[i] = &tmp8_store[i*1];
		for (i=start; i<end;i++,tmp8_store++) {
			if (prediction[i]<0)
				*tmp8_store=0;
			else
				*tmp8_store=prediction[i]-pos_prediction_values+1;
		}
		structvector[t][0].indexprediction = tmp8;
		row_indexprediction[t] = nodenumber;
		column_indexprediction[t] = 1;
    
		/* PREDICTIONS */
		nb_pred=(nodenumber+1)/2;
		/*tmp=mxCreateNumericMatrix(nb_pred,nb_goal_multiregr,mxSINGLE_CLASS,mxREAL);
		ptr_float=(float *)mxGetPr(tmp);
		for (i=0;i<nb_goal_multiregr;i++) {
			for (j=pos_prediction_values;j<pos_prediction_values+nb_pred;j++) {
				*ptr_float=prediction_values[j][i];
				ptr_float++;
			}
		}
		mxSetFieldByNumber(structvector,t,9,tmp);*/
		tmp9_store = (float *)malloc(nb_pred*nb_goal_multiregr*sizeof(float));
		tmp9 = (float **)malloc(nodenumber*sizeof(float *));
		for(i=0;i<nb_pred;i++)
			tmp9[i] = &tmp9_store[i*nb_goal_multiregr];
		for (i = 0; i< nb_goal_multiregr; i++) {
			for (j=pos_prediction_values;j<pos_prediction_values+nb_pred;j++) {
				*tmp9_store=prediction_values[j][i];
				tmp9_store++;
			}
		}
		structvector[t][0].predictions = tmp9;
		row_predictions[t] = nb_pred;
		column_predictions[t] = nb_goal_multiregr;
		
		pos_prediction_values+=nb_pred;
	}

	/* NODESIZE */
	/*tmp=mxCreateNumericMatrix(nodenumber,1,mxSINGLE_CLASS,mxREAL);
	ptr_float=(float *)mxGetPr(tmp);
	for (i=start; i<end;i++,ptr_float++)
		*ptr_float=node_size[i];
	mxSetFieldByNumber(structvector,t,10,tmp);*/
	tmp10_store = (float *)malloc(nodenumber*1*sizeof(float));
	tmp10 = (float **)malloc(nodenumber*sizeof(float *));
	for(i = 0; i< nodenumber; i++)
		tmp10[i] = &tmp10_store[i * 1];
	for (i=start; i<end;i++,tmp10_store++)
		*tmp10_store=node_size[i];
	structvector[t][0].nodesize = tmp10;

	row_nodesize[t] = nodenumber;
	column_nodesize[t] = 1;
	
	tmp_store = NULL;
	tmp = NULL;
	
	tmp1_store = NULL;
	tmp1 = NULL;
	
	tmp2_store = NULL;
	tmp2 = NULL;
	
	tmp3_store = NULL;
	tmp3 = NULL;
	
	tmp4_store = NULL;
	tmp4 = NULL;
	
	tmp5_store = NULL;
	tmp5 = NULL;
	
	tmp6_store = NULL;
	tmp6 = NULL;
	
	tmp7_store = NULL;
	tmp7 = NULL;
	
	tmp8_store = NULL;
	tmp8 = NULL;
	
	tmp9_store = NULL;
	tmp9 = NULL;
	
	tmp10_store = NULL;
	tmp10 = NULL;
}

TreeEnsembleStruct **create_struct_from_current_ensemble(int *row /* row of result matrix*/, int *column /* column of result matrix*/) {
	
	//mxArray *structvector, *structens, *tmp;
	//int t, *ptr_int,i;
	//char const* fieldnames[]={"nodenumber","testattribute","testthreshold","children","ls","objectweights","weight","savepred","indexprediction","predictions","nodesize"};
	//char const* fieldnamesens[]={"ls","trees"};
	
	int i,t;
	
	TreeStruct *structvector_store;
	TreeStruct **structvector;
	
	TreeEnsembleStruct *structens_store;
	TreeEnsembleStruct **structens;
	
	int *tmp_store;
	int **tmp;

	row_nodenumber = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_nodenumber = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_testattribute = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_testattribute = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_testthreshold = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_testthreshold = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_children = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_children = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_ls = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_ls = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_objectweights = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_objectweights = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_weight = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_weight = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_savepred = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_savepred = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_indexprediction = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_indexprediction = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_predictions = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_predictions = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	row_nodesize = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));
	column_nodesize = (int *)malloc(current_nb_of_ensemble_terms*sizeof(int));

	save_ensemble_ls_pos_2 = 0;
	pos_prediction_values = 0;
	sum_weight = 0.0;
	if (average_predictions_ltrees) {
		for (t=0; t< current_nb_of_ensemble_terms; t++)
			sum_weight += ltrees_weight[t];
	}

	//structvector = mxCreateStructMatrix(current_nb_of_ensemble_terms,1,11,fieldnames);
	structvector_store = (TreeStruct *)malloc(current_nb_of_ensemble_terms * 1 * sizeof(TreeStruct));
	/* both row and column of result matrix are one*/
	*row = current_nb_of_ensemble_terms;
	*column = 1;	
	//TODO: malloc check
	structvector = (TreeStruct **)malloc(current_nb_of_ensemble_terms * sizeof(TreeStruct *));
	//TODO: malloc check
	for(t=0; t< current_nb_of_ensemble_terms; t++)
		structvector[t] = &structvector_store[t*1];
  
	for (t=0; t<current_nb_of_ensemble_terms; t++) {
		/* cree la structure pour chaque arbre */
		create_struct_from_tree(t,structvector);
	}
	
	//structens = mxCreateStructMatrix(1,1,2,fieldnamesens);
	structens_store = (TreeEnsembleStruct *)malloc(1 * 1 * sizeof(TreeEnsembleStruct));
	//TODO: malloc check
	structens = (TreeEnsembleStruct **)malloc(1 * sizeof(TreeEnsembleStruct *));
	//TODO: malloc check
	for(t=0; t< 1; t++)
		structens[t] = &structens_store[t*1];

	//tmp=mxCreateNumericMatrix(global_learning_set_size,1,mxINT32_CLASS,mxREAL);
	tmp_store = (int *)malloc(global_learning_set_size*1*sizeof(int));
	tmp = (int **)malloc(global_learning_set_size*sizeof(int *));
	for(t=0; t<global_learning_set_size; t++)
		tmp[t] = &tmp_store[t*1];
	row_ls_TreeEnsembleStruct = global_learning_set_size;
	column_ls_TreeEnsembleStruct = 1;
	
	for (i=0; i<global_learning_set_size;i++,tmp_store++)
		*tmp_store=object_mapping[i]+1;

	//mxSetFieldByNumber(structens,0,0,tmp);
	//mxSetFieldByNumber(structens,0,1,structvector);
	structens[0][0].ls = tmp;
	structens[0][0].trees = structvector;
	
	tmp = NULL;
	tmp_store = NULL;
	
	structens_store = NULL;
	structvector_store = NULL;
	structvector = NULL;
	
	return structens;
}

double **compute_variable_imp_matlab(int *varimp_row, int *varimp_column) {
	//mxArray *varimp;
	double **varimp;
	double *varimp_store;
	
	//double *varimpptr;
	int i;
	
	/* on retrie les variables */
	for (i=0; i<nb_attributes;i++)
		attribute_vector[i] = i;
	varimp_store = (double *)malloc(nb_attributes * nb_goal_multiregr * sizeof(double));
	*varimp_row = nb_attributes;
	*varimp_column = nb_goal_multiregr;
	varimp = (double **)malloc(nb_attributes * sizeof(double *));
	for(i = 0; i< nb_attributes; i++)
		varimp[i] = &varimp_store[i * nb_goal_multiregr];
	/*varimp=mxCreateNumericMatrix(nb_attributes,nb_goal_multiregr,mxDOUBLE_CLASS,mxREAL);
	varimpptr=(double *)mxGetPr(varimp);*/
	for (i=0;i<nb_attributes*nb_goal_multiregr; i++){
		varimp_store[i]=0.0;
	}
	compute_ltrees_variable_importance_multiregr_separate(varimp_store, -1); 
	//TEST
	// printf("%d, %d\n", nb_attributes, nb_goal_multiregr);
	// for(i=0;i<nb_attributes*nb_goal_multiregr; i++)
		// printf("%40.30f\n", varimp_store[i]);
	// printf("\n");
	return varimp;
}

//jobject create_new_object_vararg(JNIEnv *env, jclass class_name, const char *parameters_signature, ...) {
	
//	va_list arg_list;

//	jmethodID method_id;
//	jobject new_object;

	/* initialize va_list*/
//	va_start(parameters_signature, arg_list);
	/* get constructor method ID*/
//	method_id = (*env)->GetMethodID(env, class_name, "<init>", parameters_signature);
	/* create new object*/
//	new_object = (*env)->NewObjectV(env, class_name, method_id, arg_list);
	/* clear up*/
//	va_end(arg_list);
//	return new_object;
//}


/*
 * Class:     birc_grni_rf_Genie3SingleTime
 * Method:    rtenslearncless
 * Signature: (II[FI[FI[II[DLbirc/grni/rf/RtEnsParam;ILbirc/grni/rf/Plhs_Genie3SingleTime_Rtenslearncless;)V
 */
JNIEXPORT void JNICALL Java_birc_grni_rf_Genie3SingleTime_rtenslearncless(JNIEnv *env, jclass cl, 
														jint xDataRow, jint xDataColumn, jfloatArray xDataStore, 
														jint yDataColumn, jfloatArray yData, /* vertical vector*/
														jint lsDataLength, jintArray lsData, 
														jint wDataLength, jdoubleArray wData, 
														jobject treeparamData /* RtEnsParam*/, jint verbose, jobject plhs /* plhs*/) {
	
	/* index*/
	int i, j, k, h;
	
	int ini_row = 0;								
	int ini_column = 0;
	
	/* input parameters related */
	int length_ls_vector;						/* size of learning set*/
	int nbweights;								/* number of weights*/
	int maxnbnodes;					
	int *ptr_lsData;							/* pointer to input learning set*/
	
	/* jclass*/
	jclass class_jintArray;
	jclass class_jfloatArray;
	jclass class_jdoubleArray;
	
	jclass class_Plhs_Genie3SingleTime_Rtenslearncless;
	jclass class_TreeStruct;
	jclass class_TreeEnsembleStruct;
	jclass class_TreeStructArray;
	jclass class_TreeEnsembleStructArray;
	
	/* jfieldID*/
	jfieldID id_treeEnsemble_Plhs;
	jfieldID id_varimp_Plhs;
	
	jfieldID id_trees_TreeEnsembleStruct;
	
	jfieldID id_nodenumber_TreeStruct;
	jfieldID id_testattribute_TreeStruct;
	jfieldID id_testthreshold_TreeStruct;
	jfieldID id_children_TreeStruct;
	jfieldID id_ls_TreeStruct;
	jfieldID id_objectweights_TreeStruct;
	jfieldID id_weight_TreeStruct;
	jfieldID id_savepred_TreeStruct;
	jfieldID id_indexprediction_TreeStruct;
	jfieldID id_predictions_TreeStruct;
	jfieldID id_nodesize_TreeStruct;
	
	/* jmethodID*/
	jmethodID method_id_TreeStruct_constructor;
	jmethodID method_id_TreeEnsembleStruct_constructor;
	
	/* isCopy*/
	jboolean isCopy = NULL;
	jboolean isCopy_object_weight = NULL;
	jboolean isCopy_core_table = NULL;
	jboolean isCopy_core_table_y = NULL;
	jboolean isCopy_ptr_lsData = NULL;
	
	/* calculation of tree ensemble*/
	jintArray nodenumber_row;
	jobjectArray nodenumber;
	int *ptr_nodenumber_row;
	jintArray testattribute_row;
	jobjectArray testattribute;
	int *ptr_testattribute_row;
	jobjectArray testthreshold;
	jfloatArray testthreshold_row;
	float *ptr_testthreshold_row;
	jobjectArray children;
	jintArray children_row;
	int *ptr_children_row;
	jobjectArray ls;
	jintArray ls_row;
	int *ptr_ls_row;
	jobjectArray objectweights;
	jfloatArray objectweights_row;
	float *ptr_objectweights_row;
	jobjectArray weight;
	jfloatArray weight_row;
	float *ptr_weight_row;
	jobjectArray savepred;
	jintArray savepred_row;
	int *ptr_savepred_row;
	jobjectArray indexprediction;
	jintArray indexprediction_row;
	int *ptr_indexprediction_row;
	jobjectArray predictions;
	jfloatArray predictions_row;
	float *ptr_predictions_row;
	jobjectArray nodesize;
	jfloatArray nodesize_row;
	float *ptr_nodesize_row;
	
	int *row_trees_TreeEnsembleStruct, *column_trees_TreeEnsembleStruct;		/* number of rows and columns of trees(matrix) in a TreeEnsembleStruct object*/
	
	jobjectArray j_treeEnsemble_row;						/* one row of treeEnsemble(matrix)*/
	jobjectArray j_treeEnsemble_matrix;						/* treeEnsemble(matrix)*/
	TreeEnsembleStruct **tree_ensemble;						/* structure create by method: create_struct_from_current_ensemble, includes the tree structure*/
	jobject obj_TreeStruct;
	jobject obj_TreeEnsembleStruct;
	
	jobjectArray ls_TreeEnsembleStruct;
	jintArray ls_TreeEnsembleStruct_row;
	int *ptr_ls_TreeEnsembleStruct_row;
	
	/* calculation of variable importance*/
	int *result_varimp_row, *result_varimp_column;			/* number of rows and columns of varimp*/
	jdoubleArray j_varimp_row;								/* one row of varimp*/
	jobjectArray j_varimp_matrix;							/* varimp(matrix)*/
	double **varimp;
	double *ptr_varimp_row;
	
	/* plhs*/
	jobjectArray Plhs_Genie3SingleTime_Rtenslearncless_tree_row, Plhs_Genie3SingleTime_Rtenslearncless_tree_matrix;
	
	srand(100); /* set random seed*/
		
	set_print_result(verbose, 0); /* set whether to print some aid information and detailed aid information*/
	
	nb_attributes = xDataColumn;
	nb_obj_in_core_table = xDataRow;
	core_table = (CORETABLE_TYPE *)((*env)->GetFloatArrayElements(env, xDataStore, isCopy_core_table));
			
	attribute_descriptors = (int *) malloc((size_t)nb_attributes*sizeof(int));
	length_attribute_descriptors=nb_attributes;
	
	// TODO:
	/*
	attdes=mxGetField(treeparamData,0,"attdesc");
	if (attdes==NULL) {
		for (i=0;i<nb_attributes;i++)
			attribute_descriptors[i]=0; 
	} else {
		ptr_int=(int *)mxGetPr(attdes);
		for (i=0; i<nb_attributes; i++,ptr_int++)
			attribute_descriptors[i]=(*ptr_int);
	}
	*/
	for (i=0;i<nb_attributes;i++)
		attribute_descriptors[i]=0;
		
	nb_classes=0;//number of classes for classification
	attribute_vector=malloc((size_t)nb_attributes*sizeof(int));
	for (i=0; i<nb_attributes;i++)
		attribute_vector[i]=i;
	
	nb_obj_in_core_table = xDataRow;
	length_ls_vector = lsDataLength;
	
	global_learning_set_size = length_ls_vector;
	current_learning_set_size = length_ls_vector;
	object_mapping = (int *)malloc((size_t)length_ls_vector*sizeof(int));
	ptr_lsData = (int *)((*env)->GetIntArrayElements(env, lsData, isCopy_ptr_lsData));
	for (i=0; i<length_ls_vector; i++) {
		object_mapping[i] = ptr_lsData[i]-1;
	}
	
	current_learning_set = (int *)malloc((size_t)length_ls_vector*sizeof(int));
	for (i=0; i<length_ls_vector; i++)
		current_learning_set[i] = i;
	
	nbweights = wDataLength;	/*number of weight data*/
	
	if (nbweights == 0) {
		object_weight=(SCORE_TYPE *)malloc((size_t)length_ls_vector*sizeof(SCORE_TYPE));
		for (i=0;i<length_ls_vector;i++)//weight vector has same length with learning set
			object_weight[i]=1.0;
	} else if (nbweights == length_ls_vector) {
		object_weight = (double *)((*env)->GetDoubleArrayElements(env, wData, isCopy_object_weight));
	} else {
		printf("wrong size of the weight vector");
		exit(1);
	}
		// TODO:
		/*
		mexErrMsgTxt("wrong size of the weight vector");
		*/
			
	/* initializes a multiple regression problem */
	nb_goal_multiregr = yDataColumn;

	core_table_y = (CORETABLE_TYPE *)((*env)->GetFloatArrayElements(env, yData, isCopy_core_table_y));
	goal_multiregr = malloc((size_t)nb_goal_multiregr*sizeof(int));
	for (i=0; i<nb_goal_multiregr; i++)
		goal_multiregr[i] = i;
	getobjy_multiregr_learn = getobjy_multiregr_learn_matlab;
	
	set_ensemble_param(env, treeparamData);
	
	init_save_ensemble_ls(1);
	
	maxnbnodes=number_of_ensemble_terms*(best_first*(2*best_first_max_nb_tests+1)+(1-best_first)*(2*length_ls_vector-1));
	allocate_tree_tables(maxnbnodes,ceil((maxnbnodes+number_of_ensemble_terms)/2),multiregr_savepred*nb_goal_multiregr,0);
	allocate_multiregr_table_score(nb_goal_multiregr);
	
	clean_all_trees();
	
	build_one_tree_ensemble(NULL, 0);
	
	//Q: assume nlhs == 2 
	if (verbose == 1){
		printf("Creation de la structure...");
    }
	
    //plhs[0] = create_struct_from_current_ensemble();
	class_Plhs_Genie3SingleTime_Rtenslearncless = (*env)->GetObjectClass(env, plhs);
	id_treeEnsemble_Plhs = (*env)->GetFieldID(env, class_Plhs_Genie3SingleTime_Rtenslearncless, "treeEnsemble", "[[Lbirc/grni/rf/TreeEnsembleStruct;");
	
	class_TreeEnsembleStruct = (*env)->FindClass(env, "birc/grni/rf/TreeEnsembleStruct");
	id_trees_TreeEnsembleStruct = (*env)->GetFieldID(env, class_TreeEnsembleStruct, "trees", "[[Lbirc/grni/rf/TreeStruct;");
	
	row_trees_TreeEnsembleStruct = &ini_row;
	column_trees_TreeEnsembleStruct = &ini_column;
	tree_ensemble = create_struct_from_current_ensemble(row_trees_TreeEnsembleStruct, column_trees_TreeEnsembleStruct);

	class_TreeStruct = (*env)->FindClass(env, "birc/grni/rf/TreeStruct");
	
	class_TreeStructArray = (*env)->FindClass(env, "[Lbirc/grni/rf/TreeStruct;");
	j_treeEnsemble_matrix = (*env)->NewObjectArray(env, *row_trees_TreeEnsembleStruct, class_TreeStructArray, NULL);
	
	method_id_TreeStruct_constructor = (*env)->GetMethodID(env, class_TreeStruct, "<init>", "([[I[[I[[F[[I[[I[[F[[F[[I[[I[[F[[F)V");
	method_id_TreeEnsembleStruct_constructor = (*env)->GetMethodID(env, class_TreeEnsembleStruct, "<init>", "([[I[[Lbirc/grni/rf/TreeStruct;)V");
	for(i = 0; i<(*row_trees_TreeEnsembleStruct); i++) {
		j_treeEnsemble_row = (*env)->NewObjectArray(env, *column_trees_TreeEnsembleStruct, class_TreeStruct, NULL);
		for(j = 0; j<(*column_trees_TreeEnsembleStruct); j++) {
			obj_TreeStruct = (*env)->NewObject(env, class_TreeStruct, method_id_TreeStruct_constructor, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
		
			/* set nodenumber*/
			
			/* create matrix*/
			class_jintArray = (*env)->FindClass(env, "[I");
			nodenumber = (*env)->NewObjectArray(env, row_nodenumber[i], class_jintArray, NULL);
			for(h = 0; h< row_nodenumber[i]; h++)
			{
				/* create array*/
				nodenumber_row = (*env)->NewIntArray(env, column_nodenumber[i]);
				/* set values to array*/
				ptr_nodenumber_row = (int *)((*env)->GetIntArrayElements(env, nodenumber_row, NULL));
				for(k = 0; k< column_nodenumber[i]; k++)
					ptr_nodenumber_row[k] = tree_ensemble[0][0].trees[i][j].nodenumber[(k * row_nodenumber[i] + h) / column_nodenumber[i]][(k * row_nodenumber[i] + h) % column_nodenumber[i]];
				(*env)->ReleaseIntArrayElements(env, nodenumber_row, ptr_nodenumber_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, nodenumber, h, nodenumber_row);
			}
		
			/* set testattribute*/
			
			/* create matrix*/
			testattribute = (*env)->NewObjectArray(env, row_testattribute[i], class_jintArray, NULL);
			for(h = 0; h< row_testattribute[i]; h++)
			{
				/* create array*/
				testattribute_row = (*env)->NewIntArray(env, column_testattribute[i]);
				/* set values to array*/
				ptr_testattribute_row = (int *)((*env)->GetIntArrayElements(env, testattribute_row, NULL));
				for(k = 0; k< column_testattribute[i]; k++)
					ptr_testattribute_row[k] = tree_ensemble[0][0].trees[i][j].testattribute[(k * row_testattribute[i] + h) / column_testattribute[i]][(k * row_testattribute[i] + h) % column_testattribute[i]];
				(*env)->ReleaseIntArrayElements(env, testattribute_row, ptr_testattribute_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, testattribute, h, testattribute_row);
			}
			
			/* set testthreshold*/
				
			/* create matrix*/
			class_jfloatArray = (*env)->FindClass(env, "[F");
			testthreshold = (*env)->NewObjectArray(env, row_testthreshold[i], class_jfloatArray, NULL);
			for(h = 0; h< row_testthreshold[i]; h++)
			{
				/* create array*/
				testthreshold_row = (*env)->NewFloatArray(env, column_testthreshold[i]);
				/* set values to array*/
				ptr_testthreshold_row = (float *)((*env)->GetFloatArrayElements(env, testthreshold_row, NULL));
				for(k = 0; k< column_testthreshold[i]; k++)
					ptr_testthreshold_row[k] = tree_ensemble[0][0].trees[i][j].testthreshold[(k * row_testthreshold[i] + h) / column_testthreshold[i]][(k * row_testthreshold[i] + h) % column_testthreshold[i]];
				(*env)->ReleaseFloatArrayElements(env, testthreshold_row, ptr_testthreshold_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, testthreshold, h, testthreshold_row);
			}
			
			/* set children*/
				
			/* create matrix*/
			children = (*env)->NewObjectArray(env, row_children[i], class_jintArray, NULL);
			for(h = 0; h< row_children[i]; h++)
			{
				/* create array*/
				children_row = (*env)->NewIntArray(env, column_children[i]);
				/* set values to array*/
				ptr_children_row = (int *)((*env)->GetIntArrayElements(env, children_row, NULL));
				for(k = 0; k< column_children[i]; k++)
					ptr_children_row[k] = tree_ensemble[0][0].trees[i][j].children[(k * row_children[i] + h) / column_children[i]][(k * row_children[i] + h) % column_children[i]];
				(*env)->ReleaseIntArrayElements(env, children_row, ptr_children_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, children, h, children_row);
			}
		
			/* set ls*/
				
			/* create matrix*/
			ls = (*env)->NewObjectArray(env, row_ls[i], class_jintArray, NULL);
			for(h = 0; h< row_ls[i]; h++)
			{
				/* create array*/
				ls_row = (*env)->NewIntArray(env, column_ls[i]);
				/* set values to array*/
				ptr_ls_row = (int *)((*env)->GetIntArrayElements(env, ls_row, NULL));
				for(k = 0; k< column_ls[i]; k++)
					ptr_ls_row[k] = tree_ensemble[0][0].trees[i][j].ls[(k * row_ls[i] + h) / column_ls[i]][(k * row_ls[i] + h) % column_ls[i]];
				(*env)->ReleaseIntArrayElements(env, ls_row, ptr_ls_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, ls, h, ls_row);
			}
			
			/* set objectweights*/
				
			/* create matrix*/
			objectweights = (*env)->NewObjectArray(env, row_objectweights[i], class_jfloatArray, NULL);
			for(h = 0; h< row_objectweights[i]; h++)
			{
				/* create array*/
				objectweights_row = (*env)->NewFloatArray(env, column_objectweights[i]);
				/* set values to array*/
				ptr_objectweights_row = (float *)((*env)->GetFloatArrayElements(env, objectweights_row, NULL));
				for(k = 0; k< column_objectweights[i]; k++)
					ptr_objectweights_row[k] = tree_ensemble[0][0].trees[i][j].objectweights[(k * row_objectweights[i] + h) / column_objectweights[i]][(k * row_objectweights[i] + h) % column_objectweights[i]];
				(*env)->ReleaseFloatArrayElements(env, objectweights_row, ptr_objectweights_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, objectweights, h, objectweights_row);
			}
			
			/* set weight*/
				
			/* create matrix*/
			weight = (*env)->NewObjectArray(env, row_weight[i], class_jfloatArray, NULL);
			for(h = 0; h< row_weight[i]; h++)
			{
				/* create array*/
				weight_row = (*env)->NewFloatArray(env, column_weight[i]);
				/* set values to array*/
				ptr_weight_row = (float *)((*env)->GetFloatArrayElements(env, weight_row, NULL));
				for(k = 0; k< column_weight[i]; k++)
					ptr_weight_row[k] = tree_ensemble[0][0].trees[i][j].weight[(k * row_weight[i] + h) / column_weight[i]][(k * row_weight[i] + h) % column_weight[i]];
				(*env)->ReleaseFloatArrayElements(env, weight_row, ptr_weight_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, weight, h, weight_row);
			}
			
			/* set savepred*/
				
			/* create matrix*/
			savepred = (*env)->NewObjectArray(env, row_savepred[i], class_jintArray, NULL);
			for(h = 0; h< row_savepred[i]; h++)
			{
				/* create array*/
				savepred_row = (*env)->NewIntArray(env, column_savepred[i]);
				/* set values to array*/
				ptr_savepred_row = (int *)((*env)->GetIntArrayElements(env, savepred_row, NULL));
				for(k = 0; k< column_savepred[i]; k++)
					ptr_savepred_row[k] = tree_ensemble[0][0].trees[i][j].savepred[(k * row_savepred[i] + h) / column_savepred[i]][(k * row_savepred[i] + h) % column_savepred[i]];
				(*env)->ReleaseIntArrayElements(env, savepred_row, ptr_savepred_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, savepred, h, savepred_row);
			}
			
			/* set indexprediction*/
				
			/* create matrix*/
			indexprediction = (*env)->NewObjectArray(env, row_indexprediction[i], class_jintArray, NULL);
			for(h = 0; h< row_indexprediction[i]; h++)
			{
				/* create array*/
				indexprediction_row = (*env)->NewIntArray(env, column_indexprediction[i]);
				/* set values to array*/
				ptr_indexprediction_row = (int *)((*env)->GetIntArrayElements(env, indexprediction_row, NULL));
				for(k = 0; k< column_indexprediction[i]; k++)
					ptr_indexprediction_row[k] = tree_ensemble[0][0].trees[i][j].indexprediction[(k * row_indexprediction[i] + h) / column_indexprediction[i]][(k * row_indexprediction[i] + h) % column_indexprediction[i]];
				(*env)->ReleaseIntArrayElements(env, indexprediction_row, ptr_indexprediction_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, indexprediction, h, indexprediction_row);
			}
			
			/* set predictions*/
				
			/* create matrix*/
			predictions = (*env)->NewObjectArray(env, row_predictions[i], class_jfloatArray, NULL);
			for(h = 0; h< row_predictions[i]; h++)
			{
				/* create array*/
				predictions_row = (*env)->NewFloatArray(env, column_predictions[i]);
				/* set values to array*/
				ptr_predictions_row = (float *)((*env)->GetFloatArrayElements(env, predictions_row, NULL));
				for(k = 0; k< column_predictions[i]; k++)
					ptr_predictions_row[k] = tree_ensemble[0][0].trees[i][j].predictions[(k * row_predictions[i] + h) / column_predictions[i]][(k * row_predictions[i] + h) % column_predictions[i]];
				(*env)->ReleaseFloatArrayElements(env, predictions_row, ptr_predictions_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, predictions, h, predictions_row);
			}
			
			/* set nodesize*/
				
			/* create matrix*/
			nodesize = (*env)->NewObjectArray(env, row_nodesize[i], class_jfloatArray, NULL);
			for(h = 0; h< row_nodesize[i]; h++)
			{
				/* create array*/
				nodesize_row = (*env)->NewFloatArray(env, column_nodesize[i]);
				/* set values to array*/
				ptr_nodesize_row = (float *)((*env)->GetFloatArrayElements(env, nodesize_row, NULL));
				for(k = 0; k< column_nodesize[i]; k++)
					ptr_nodesize_row[k] = tree_ensemble[0][0].trees[i][j].nodesize[(k * row_nodesize[i] + h) / column_nodesize[i]][(k * row_nodesize[i] + h) % column_nodesize[i]];
				(*env)->ReleaseFloatArrayElements(env, nodesize_row, ptr_nodesize_row, 0);
				/* set row to matrix*/
				(*env)->SetObjectArrayElement(env, nodesize, h, nodesize_row);
			}
			
			/* set each field of the TreeStruct object*/
			id_nodenumber_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "nodenumber", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_nodenumber_TreeStruct, nodenumber);
			id_testattribute_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "testattribute", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_testattribute_TreeStruct, testattribute);
			id_testthreshold_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "testthreshold", "[[F");
			(*env)->SetObjectField(env, obj_TreeStruct, id_testthreshold_TreeStruct, testthreshold);
			id_children_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "children", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_children_TreeStruct, children);
			id_ls_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "ls", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_ls_TreeStruct, ls);
			id_objectweights_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "objectweights", "[[F");
			(*env)->SetObjectField(env, obj_TreeStruct, id_objectweights_TreeStruct, objectweights);
			id_weight_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "weight", "[[F");
			(*env)->SetObjectField(env, obj_TreeStruct, id_weight_TreeStruct, weight);
			id_savepred_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "savepred", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_savepred_TreeStruct, savepred);
			id_indexprediction_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "indexprediction", "[[I");
			(*env)->SetObjectField(env, obj_TreeStruct, id_indexprediction_TreeStruct, indexprediction);
			id_predictions_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "predictions", "[[F");
			(*env)->SetObjectField(env, obj_TreeStruct, id_predictions_TreeStruct, predictions);
			id_nodesize_TreeStruct = (*env)->GetFieldID(env, class_TreeStruct, "nodesize", "[[F");
			(*env)->SetObjectField(env, obj_TreeStruct, id_nodesize_TreeStruct, nodesize);
				
			/* add the whole TreeStruct object to the matrix*/
			(*env)->SetObjectArrayElement(env, j_treeEnsemble_row, j, obj_TreeStruct);
		}
		(*env)->SetObjectArrayElement(env, j_treeEnsemble_matrix, i, j_treeEnsemble_row);
	}
	
	/* construct ls matrix of TreeEnsembleStruct in java*/
	ls_TreeEnsembleStruct = (*env)->NewObjectArray(env, row_ls_TreeEnsembleStruct, class_jintArray, NULL);
	for(i = 0; i< row_ls_TreeEnsembleStruct; i++)
	{
		ls_TreeEnsembleStruct_row = (*env)->NewIntArray(env, column_ls_TreeEnsembleStruct);
		ptr_ls_TreeEnsembleStruct_row = (*env)->GetIntArrayElements(env, ls_TreeEnsembleStruct_row, NULL);
		for(j = 0; j< column_ls_TreeEnsembleStruct; j++)
			ptr_ls_TreeEnsembleStruct_row[j] = tree_ensemble[0][0].ls[(j * row_ls_TreeEnsembleStruct + i) / column_ls_TreeEnsembleStruct][(j * row_ls_TreeEnsembleStruct + i) % column_ls_TreeEnsembleStruct];
		(*env)->ReleaseIntArrayElements(env, ls_TreeEnsembleStruct_row, ptr_ls_TreeEnsembleStruct_row, 0);
		(*env)->SetObjectArrayElement(env, ls_TreeEnsembleStruct, i, ls_TreeEnsembleStruct_row);
	}

	obj_TreeEnsembleStruct = (*env)->NewObject(env, class_TreeEnsembleStruct, method_id_TreeEnsembleStruct_constructor, ls_TreeEnsembleStruct, j_treeEnsemble_matrix);
	// in Plhs_Genie3SingleTime_Rtenslearncless object, the treeEnsemble(matrix) have only one element actually
	Plhs_Genie3SingleTime_Rtenslearncless_tree_row = (*env)->NewObjectArray(env, 1, class_TreeEnsembleStruct, obj_TreeEnsembleStruct);
	class_TreeEnsembleStructArray = (*env)->GetObjectClass(env, Plhs_Genie3SingleTime_Rtenslearncless_tree_row);
	Plhs_Genie3SingleTime_Rtenslearncless_tree_matrix = (*env)->NewObjectArray(env, 1, class_TreeEnsembleStructArray, Plhs_Genie3SingleTime_Rtenslearncless_tree_row);
	(*env)->SetObjectField(env, plhs, id_treeEnsemble_Plhs, Plhs_Genie3SingleTime_Rtenslearncless_tree_matrix);
	
    if (verbose==1){
		printf("\n");
		// TODO: mexPrintf("\n");
    }
    //if (nlhs>1){
		if (verbose==1){
			printf("Calcul de l'importance des variables...");
			// TODO: mexPrintf("Calcul de l'importance des variables...");	
		}
		//plhs[1]=compute_variable_imp_matlab();
		id_varimp_Plhs = (*env)->GetFieldID(env, class_Plhs_Genie3SingleTime_Rtenslearncless, "varimp", "[[D");
		result_varimp_row = &ini_row;
		result_varimp_column = &ini_column;
		varimp = compute_variable_imp_matlab(result_varimp_row, result_varimp_column);
		class_jdoubleArray = (*env)->FindClass(env, "[D");
		j_varimp_matrix = (*env)->NewObjectArray(env, *result_varimp_row, class_jdoubleArray, NULL);
		for(i=0; i<(*result_varimp_row); i++) {
			j_varimp_row = (*env)->NewDoubleArray(env, *result_varimp_column);
			ptr_varimp_row = (double *)((*env)->GetDoubleArrayElements(env, j_varimp_row, isCopy));
			for(j=0; j<(*result_varimp_column); j++)
				ptr_varimp_row[j] = varimp[i][j];
			(*env)->ReleaseDoubleArrayElements(env, j_varimp_row, ptr_varimp_row, 0);
			(*env)->SetObjectArrayElement(env, j_varimp_matrix, i, j_varimp_row);
		}
		(*env)->SetObjectField(env, plhs, id_varimp_Plhs, j_varimp_matrix);
		//free(varimp[0]);
		//free(varimp);
		if (verbose==1){
			// TODO: mexPrintf("\n");
		}
	//}
	
	if (verbose==1) {
		//TODO: mexPrintf("Liberation de la memoire\n");
	}
	/*mxFree(attribute_descriptors);
	mxFree(attribute_vector);
	mxFree(goal_multiregr);
	mxFree(object_mapping);
	mxFree(current_learning_set);*/
	
	free(attribute_descriptors);
	free(attribute_vector);
	free(goal_multiregr);
	free(object_mapping);
	free(current_learning_set);
	
	free_tree_tables();
	free((int *)save_ensemble_ls_vector);
	save_ensemble_ls_vector=NULL;
	free((float *)save_ensemble_ls_weight);
	save_ensemble_ls_weight=NULL;
	
	if(isCopy_core_table)
		(*env)->ReleaseFloatArrayElements(env, xDataStore, core_table, JNI_ABORT);//original experiment data matrix, no need to copy back
	else
		core_table = NULL;
	
	if(isCopy_core_table_y)
		(*env)->ReleaseFloatArrayElements(env, yData, core_table_y, JNI_ABORT);
	else
		core_table_y = NULL;
	
	if(isCopy_object_weight != NULL)
	{
		if(isCopy_object_weight)
			(*env)->ReleaseDoubleArrayElements(env, wData, object_weight, 0);
		else
			object_weight = NULL;
	}
	else
	{
		free(object_weight);
	}
	
	if(isCopy_ptr_lsData)
		(*env)->ReleaseIntArrayElements(env, lsData, ptr_lsData, JNI_ABORT);
	else
		ptr_lsData = NULL;
	
	free(tree_ensemble[0][0].trees[0]);
	tree_ensemble[0][0].trees = NULL;
	free(tree_ensemble[0][0].ls[0]);
	tree_ensemble[0][0].ls = NULL;
	free(tree_ensemble[0]);
	tree_ensemble = NULL;
	
	free(row_nodenumber);
	free(column_nodenumber);
	
	free(row_testattribute);
	free(column_testattribute);

	free(row_testthreshold);
	free(column_testthreshold);

	free(row_children);
	free(column_children);

	free(row_ls);
	free(column_ls);

	free(row_objectweights);
	free(column_objectweights);

	free(row_weight);
	free(column_weight);

	free(row_savepred);
	free(column_savepred);

	free(row_indexprediction);
	free(column_indexprediction);

	free(row_predictions);
	free(column_predictions);

	free(row_nodesize);
	free(column_nodesize);
	
	return;
}												
