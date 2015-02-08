#include <stdio.h>
#include <stdlib.h>
#include "birc_grni_en_ElasticNet.h"

extern void elnet_(
				/* input*/
				int *ka, float *parm, int *no, int *ni, float x[], float y[], float w[], int jd[], float vp[], int *ne, int *nx, 
				int *nlam, float *flmin, float ulam[], float *thr, int *isd, 
				/* output*/
				int *lmu, float a0[], float ca[], int ia[], int nin[], float rsq[], float alm[], int *nlp, int *jerr);
	
/*
 * Class:     birc_grni_en_ElasticNet
 * Method:    elnet
 * Signature: (F[FII[F[I[FIIIF[FFI[FILbirc/grni/en/ElnetResult;)V
 */
JNIEXPORT void JNICALL Java_birc_grni_en_ElasticNet_elnet(JNIEnv *env, jclass cl, 
												jfloat parm, 
												jfloatArray x,
												jint x_row,
												jint x_column,
												jfloatArray y, 
												jintArray jd, 
												jfloatArray vp, 
												jint ne, 
												jint nx, 
												jint nlam, 
												jfloat flmin, 
												jfloatArray ulam, 
												jfloat thr, 
												jint isd, 
												jfloatArray w, 
												jint ka, 
												jobject jobject_ElnetResult)
{
	/* index*/
	int i;
	
	/* native pointer to elnet input*/
	jfloat *ptr_x;
	jfloat *ptr_y;
	jfloat *ptr_w;
	jint *ptr_jd;
	jfloat *ptr_vp;
	jfloat *ptr_ulam;
	
	/* native pointer to elnet output*/
	int *lmu = NULL;
	int init_lmu;			/* initial value of lmu*/
	float *a0 = NULL;
	float *ca = NULL;
	int *ia = NULL;
	int *nin = NULL;
	float *rsq = NULL;
	float *alm = NULL;
	int *nlp = NULL;
	int init_nlp;			/* initial value of nlp*/
	int *jerr = NULL;
	int init_jerr;			/* initial value of jerr*/
	
	/* jclass*/
	jclass jclass_ElnetResult; 

	/* jmethodID*/
	jmethodID jmethodID_ElnetResult_constructor;
	jmethodID jmethodID_setA0;
	jmethodID jmethodID_setCaOneDim;
	jmethodID jmethodID_setIa;
	jmethodID jmethodID_setNin;
	jmethodID jmethodID_setRsq;
	jmethodID jmethodID_setAlm;
	jmethodID jmethodID_setNlp;
	jmethodID jmethodID_setJerr;

	/* jXXXArray*/
	jdoubleArray ElnetResult_a0;
	jdoubleArray ElnetResult_caOneDim;
	jdoubleArray ElnetResult_ia;
	jdoubleArray ElnetResult_nin;
	jdoubleArray ElnetResult_rsq;
	jdoubleArray ElnetResult_alm;

	/* pointer to jXXXArray*/
	jdouble *ptr_ElnetResult_a0;
	jdouble *ptr_ElnetResult_caOneDim;
	jdouble *ptr_ElnetResult_ia;
	jdouble *ptr_ElnetResult_nin;
	jdouble *ptr_ElnetResult_rsq;
	jdouble *ptr_ElnetResult_alm;
	
	/* get native pointer to elnet input*/
	ptr_x = (*env)->GetFloatArrayElements(env, x, 0);
	ptr_y = (*env)->GetFloatArrayElements(env, y, 0);
	ptr_w = (*env)->GetFloatArrayElements(env, w, 0);
	ptr_jd = (*env)->GetIntArrayElements(env, jd, 0);
	ptr_vp = (*env)->GetFloatArrayElements(env, vp, 0);
	ptr_ulam = (*env)->GetFloatArrayElements(env, ulam, 0);
	
	/* allocate memory for output*/
	a0 = malloc(nlam * sizeof(float));
	for(i = 0; i< nlam; i++)
		a0[i] = 0.0;
	ca = malloc(nx * nlam * sizeof(float));
	for(i = 0; i< nx * nlam; i++)
		ca[i] = 0.0;
	rsq = malloc(nlam * sizeof(float));
	for(i = 0; i< nlam; i++)
		rsq[i] = 0.0;
	ia = malloc(nx * sizeof(int));
	for(i = 0; i< nx; i++)
		ia[i] = 0;
	nin = malloc(nlam * sizeof(int));
	for(i = 0; i< nlam; i++)
		nin[i] = 0;
	alm = malloc(nlam * sizeof(float));
	for(i = 0; i< nlam; i++)
		alm[i] = 0.0;
	
	init_lmu = 0;
	lmu = &init_lmu;
	
	init_nlp = 0;
	nlp = &init_nlp;
	
	init_jerr = 0;
	jerr = &init_jerr;
	
	/* call elnet*/
	elnet_(
		/* input*/
		&ka, &parm, &x_row, &x_column, ptr_x, ptr_y, ptr_w, ptr_jd, ptr_vp, &ne, &nx, &nlam, &flmin, ptr_ulam, &thr, &isd, 
		/* output*/
		lmu, a0, ca, ia, nin, rsq, alm, nlp, jerr);
	
	jclass_ElnetResult = (*env)->FindClass(env, "birc/grni/en/ElnetResult");									/* find ELnetResult class type*/
	
	/* copy back elnet output got from fortran to java*/
	/* a0*/
	ElnetResult_a0 = (*env)->NewDoubleArray(env, *lmu);											/* create java array*/
	ptr_ElnetResult_a0 = (*env)->GetDoubleArrayElements(env, ElnetResult_a0, 0); 				/* copy fortran elnet output to newly-created java array*/
	for(i = 0; i< (*lmu); i++)
		ptr_ElnetResult_a0[i] = a0[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_a0, ptr_ElnetResult_a0, 0);
	jmethodID_setA0 = (*env)->GetMethodID(env, jclass_ElnetResult, "setA0", "([DI)V");			/* call set method in java*/
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setA0, ElnetResult_a0, *lmu);
	
	/* caOneDim*/
	ElnetResult_caOneDim = (*env)->NewDoubleArray(env, nx * (*lmu));
	ptr_ElnetResult_caOneDim = (*env)->GetDoubleArrayElements(env, ElnetResult_caOneDim, 0);
	for(i = 0; i< nx * (*lmu); i++)
		ptr_ElnetResult_caOneDim[i] = ca[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_caOneDim, ptr_ElnetResult_caOneDim, 0);
	jmethodID_setCaOneDim = (*env)->GetMethodID(env, jclass_ElnetResult, "setCaOneDim", "([DII)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setCaOneDim, ElnetResult_caOneDim, nx, *lmu);
	
	/* ia*/
	ElnetResult_ia = (*env)->NewDoubleArray(env, nx);
	ptr_ElnetResult_ia = (*env)->GetDoubleArrayElements(env, ElnetResult_ia, 0);
	for(i = 0; i< nx; i++)
		ptr_ElnetResult_ia[i] = ia[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_ia, ptr_ElnetResult_ia, 0);
	jmethodID_setIa = (*env)->GetMethodID(env, jclass_ElnetResult, "setIa", "([DI)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setIa, ElnetResult_ia, nx);
	
	/* nin*/
	ElnetResult_nin = (*env)->NewDoubleArray(env, *lmu);
	ptr_ElnetResult_nin = (*env)->GetDoubleArrayElements(env, ElnetResult_nin, 0);
	for(i = 0; i< (*lmu); i++)
		ptr_ElnetResult_nin[i] = nin[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_nin, ptr_ElnetResult_nin, 0);
	jmethodID_setNin = (*env)->GetMethodID(env, jclass_ElnetResult, "setNin", "([DI)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setNin, ElnetResult_nin, *lmu);
	
	/* rsq*/
	ElnetResult_rsq = (*env)->NewDoubleArray(env, *lmu);
	ptr_ElnetResult_rsq = (*env)->GetDoubleArrayElements(env, ElnetResult_rsq, 0);
	for(i = 0; i< (*lmu); i++)
		ptr_ElnetResult_rsq[i] = rsq[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_rsq, ptr_ElnetResult_rsq, 0);
	jmethodID_setRsq = (*env)->GetMethodID(env, jclass_ElnetResult, "setRsq", "([DI)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setRsq, ElnetResult_rsq, *lmu);
	
	/* alm*/
	ElnetResult_alm = (*env)->NewDoubleArray(env, *lmu);
	ptr_ElnetResult_alm = (*env)->GetDoubleArrayElements(env, ElnetResult_alm, 0);
	for(i = 0; i< (*lmu); i++)
		ptr_ElnetResult_alm[i] = alm[i];
	(*env)->ReleaseDoubleArrayElements(env, ElnetResult_alm, ptr_ElnetResult_alm, 0);
	jmethodID_setAlm = (*env)->GetMethodID(env, jclass_ElnetResult, "setAlm", "([DI)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setAlm, ElnetResult_alm, *lmu);
	
	/* nlp*/
	jmethodID_setNlp = (*env)->GetMethodID(env, jclass_ElnetResult, "setNlp", "(I)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setNlp, *nlp);
	
	/* jerr*/
	jmethodID_setJerr = (*env)->GetMethodID(env, jclass_ElnetResult, "setJerr", "(I)V");
	(*env)->CallVoidMethod(env, jobject_ElnetResult, jmethodID_setJerr, *jerr);
	
	/* deallocate*/
	(*env)->ReleaseFloatArrayElements(env, x, ptr_x, JNI_ABORT);
	(*env)->ReleaseFloatArrayElements(env, y, ptr_y, JNI_ABORT);
	(*env)->ReleaseFloatArrayElements(env, w, ptr_w, JNI_ABORT);
	(*env)->ReleaseIntArrayElements(env, jd, ptr_jd, JNI_ABORT);
	(*env)->ReleaseFloatArrayElements(env, vp, ptr_vp, JNI_ABORT);
	(*env)->ReleaseFloatArrayElements(env, ulam, ptr_ulam, JNI_ABORT);
	
	//Q:
	// free(lmu);
	// free(a0);
	// free(ca);
	// free(ia);
	// free(nin);
	// free(rsq);
	// free(alm);
	// free(nlp);
	// free(jerr);
}
