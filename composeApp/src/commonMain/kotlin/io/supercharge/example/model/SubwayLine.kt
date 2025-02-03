package io.supercharge.example.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import io.supercharge.example.util.decodeToPolyline

@Stable
enum class SubwayLine(
    val polyline: List<Coordinate>,
    val color: Color
) {

    M1(
        """md`aHasosB@?AFIvHAdABz@DdALhAFp@RdAX`Av@rBl@lBdEdM`@|ANtACj@FdAGbDD?E?@`B?LFv@Fb@D^Nl@HVFRLVN\PVPVf@r@DDFFh@d@FBTFZLPFv@Vb@Jb@RZRp@f@`@Zb@ZTTPPvA~BCBBCBDrO|WX\A@@AzK~QFLIJHKbKdQFT?F?GhJrOlAvBFHCBBCpOzWLRA?@?vMxTCBBCBBn@pAVPPFVDpDO\?PFNFJFHRHTFd@Bb@J\Dr@AC@BFn@~@~MFNHJJBNBX@HDJHDL`@pABNAA"""
            .decodeToPolyline(),
        Color(0xFFFFD800)
    ),

    M2(
        """_||`HilxsBFCPfDJbDBvBB~AnBv^dAxSl@rKj@xI@TT|DHbADd@Hv@AA@@BPR|A`@zC\`CNvAF`ADv@ApAIzBM|BMtB?~AF~B@bA@n@NxBT|D@bABhAEhBM~DEvA?v@Bp@FrAd@nIDp@@AA@H`Bp@dFTvDHnBDpB@nB@z@EpD?xD@p|@@lRC~ICnDEdG?f@BAC@Ah@DhAHtAHlALfANrATpAHh@bAzFnAbHlA~GjApGr@fE|@fElBlJNx@@CAB|Gj`@|@hFZzB^zCBCCBb@lDDn@Bh@@f@@b@Et@I~@Mz@Kt@Mz@Qv@W~@Uv@Wx@iArB[d@c@f@w@v@{CpC_@Zq@j@i@V{Cx@w@PcAP@HAIG?w@RmBh@wGnCkDbByDzBmA|@aBbBq@dA[f@cAxAc@fAq@dBOd@[~@M`@UtAMdAc@~Gk@pIBAC@_B~UQtBQtAiAbFqBhI@@AAkDpNo@dCc@~BUlAKx@Gp@G`AE~@AfA?dADlAHvABt@Fr@R`BXjBb@jBXnAd@xAZt@Zx@p@hAnCjEHJ@EAD^d@X`@\d@d@d@f@b@p@`@v@^nAZbAPnBLxAC`AGfAMrAY~@Sz@_@fEoAvEy@v@Sr@UAE"""
            .decodeToPolyline(),
        Color(0xFFE41F18)
    ),

    M3(
        """{hu`HmuzsBAAaBnIq@nDOv@Ij@OhAKv@M|AObEE|AExBChASdGChBIrFCrCAl@Bt@Dv@NdCXdDLfADjBDnB@vAAv@GjCU|Fq@tJWpCMdA{@bFw@~EQnA@@AACN_@hDy@zDkCvMk@jCkD|MAC@BI^sH`ZaE~OCEBDu@nC}@`DwApEs@lBc@lAkKb\_@vAYnA{BfKADA?@?_AlEoDfNq@`C]hAwF~RcCdIKd@D@EAqAzE{Mlf@s@pDyEnUu@dDDBECuAlGkCbL{CdNSx@D@EAYjAWbAm@xA}DrI_FlLoBzEe@hAa@tA}FtSB@CA_DbLo@bBo@pAy@pAk@p@iDhDBFCGCDq@\oAd@aFlAiEp@cG|@aAL{F\WBAB@CqBFqB?_IQ{CEuIBiDO?D?EmCM{DImESiCMaBMw@K}@Qu@Qy@WaAe@k@[yGaFCBBCECqCyA{DiC}EaDcC_BcAi@WM[IeBg@eBk@oA]gCw@oCeAMA?B?CWGu@KcF{AyGyBiGwBuDiAgBi@yDwA[K?A?@uC_AgFiBaLeD_FwA_OgFUI?E?DoA_@u@]eAi@{CeAcGcByCy@{KuDAC@B}Bw@mFyAo[yIcWcH?E?D]KsOqEmKaDkQkFSI[Sa@WOGYSq@m@SYUa@Ym@M]kAsEa@oAeBoHEW?E?Dw@iCS}@Y_Bc@_CQ_AM}@QcBG_AEu@E{@GoBC_B?cAFwEHaCP{CFmAf@sIBWBA"""
            .decodeToPolyline(),
        Color(0xFF0067AA)
    ),

    M4(
        """uj|`HoumsB?Fh@hAf@v@x@`AdA~@x@p@l@XpQfJCCBBp@^bBhAhA~@t@p@v@fAt@zAd@fA\~A`@pBTjBt@jKBAC@BVRnCX`Cr@|CpFtQ|BfJbB~F~@hCfCdGAC@BjDhIx@rAlAlA`Ar@xFvCCIBHjFjD~@|@nArAjAxAd@~@fCfFAA@@rD|H|I|P|@xAx@lAhBdBv@j@rAf@x@V`FpARF?C?BnVdG?K?JH@jMlDtA`@fAl@rAfArBrB~@nAz@rAz@fBj@pAX|@p@dCbA`FhKxm@@CAB?DT`BbA`IPzBPzCHpCF`IErDSnL]vNKrF?F"""
            .decodeToPolyline(),
        Color(0xFF4CA22F)
    )
}
