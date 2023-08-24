var Ze=Object.defineProperty,Je=Object.defineProperties;var et=Object.getOwnPropertyDescriptors;var Ee=Object.getOwnPropertySymbols;var tt=Object.prototype.hasOwnProperty,ot=Object.prototype.propertyIsEnumerable;var Te=(e,o,i)=>o in e?Ze(e,o,{enumerable:!0,configurable:!0,writable:!0,value:i}):e[o]=i,y=(e,o)=>{for(var i in o||(o={}))tt.call(o,i)&&Te(e,i,o[i]);if(Ee)for(var i of Ee(o))ot.call(o,i)&&Te(e,i,o[i]);return e},ce=(e,o)=>Je(e,et(o));var fe=(e,o,i)=>new Promise((g,d)=>{var O=c=>{try{u(i.next(c))}catch(p){d(p)}},a=c=>{try{u(i.throw(c))}catch(p){d(p)}},u=c=>c.done?g(c.value):Promise.resolve(c.value).then(O,a);u((i=i.apply(e,o)).next())});import{d as ae,u as Se,_ as le,B as nt,p as ge,S as st,aw as it,aJ as De,e as rt,f as at,aA as lt,E as ut,G as _t,av as ct,a2 as P,aK as Ae,aL as de,aM as B,Q as se,R as X,aN as Ce,am as ie,af as he,aO as Le,aP as Be,ac as we,aQ as gt,aR as dt,aS as Ot,al as St,U as pt,I as Et}from"./index.js";import{d as q,e as C,a7 as N,_ as T,$ as m,F as V,R as D,a8 as j,a9 as M,E as Oe,a1 as re,ac as Y,ab as x,a2 as U,af as Ue,a0 as Me,f as s,k as Q,g as me,u as t,J as K,w as Ne,q as Tt,v as ft,ad as Re,t as Dt,aa as At,ai as Ct,aj as Mt,r as Pe}from"./vue-50936e44.js";import{aQ as Nt,aE as Rt,C as It,aq as w}from"./antd-685f22a1.js";import{c as F,u as yt,a as bt}from"./index-fd2c8e81.js";const{t:Ie}=ae(),Fe={confirmLoading:{type:Boolean},showCancelBtn:{type:Boolean,default:!0},cancelButtonProps:Object,cancelText:{type:String,default:Ie("common.cancelText")},showOkBtn:{type:Boolean,default:!0},okButtonProps:Object,okText:{type:String,default:Ie("common.okText")},okType:{type:String,default:"primary"},showFooter:{type:Boolean},footerHeight:{type:[String,Number],default:60}},ht=y({isDetail:{type:Boolean},title:{type:String,default:""},loadingText:{type:String},showDetailBack:{type:Boolean,default:!0},visible:{type:Boolean},loading:{type:Boolean},maskClosable:{type:Boolean,default:!0},getContainer:{type:[Object,String]},closeFunc:{type:[Function,Object],default:null},destroyOnClose:{type:Boolean}},Fe),Lt=q({name:"BasicDrawerFooter",props:ce(y({},Fe),{height:{type:String,default:"60px"}}),emits:["ok","close"],setup(e,{emit:o}){const{prefixCls:i}=Se("basic-drawer-footer"),g=C(()=>{const a=`${e.height}`;return{height:a,lineHeight:`calc(${a} - 1px)`}});function d(){o("ok")}function O(){o("close")}return{handleOk:d,prefixCls:i,handleClose:O,getStyle:g}}});function Bt(e,o,i,g,d,O){const a=N("a-button");return e.showFooter||e.$slots.footer?(T(),m("div",{key:0,class:U(e.prefixCls),style:Ue(e.getStyle)},[e.$slots.footer?D(e.$slots,"footer",{key:1}):(T(),m(V,{key:0},[D(e.$slots,"insertFooter"),e.showCancelBtn?(T(),j(a,Y({key:0},e.cancelButtonProps,{onClick:e.handleClose,class:"mr-2"}),{default:M(()=>[Oe(re(e.cancelText),1)]),_:1},16,["onClick"])):x("",!0),D(e.$slots,"centerFooter"),e.showOkBtn?(T(),j(a,Y({key:1,type:e.okType,onClick:e.handleOk},e.okButtonProps,{class:"mr-2",loading:e.confirmLoading}),{default:M(()=>[Oe(re(e.okText),1)]),_:1},16,["type","onClick","loading"])):x("",!0),D(e.$slots,"appendFooter")],64))],6)):x("",!0)}const wt=le(Lt,[["render",Bt]]),Ut=q({name:"BasicDrawerHeader",components:{BasicTitle:nt,ArrowLeftOutlined:Nt},props:{isDetail:ge.bool,showDetailBack:ge.bool,title:ge.string},emits:["close"],setup(e,{emit:o}){const{prefixCls:i}=Se("basic-drawer-header");function g(){o("close")}return{prefixCls:i,handleClose:g}}});const mt={key:1};function Pt(e,o,i,g,d,O){const a=N("BasicTitle"),u=N("ArrowLeftOutlined");return e.isDetail?(T(),m("div",{key:1,class:U([e.prefixCls,`${e.prefixCls}--detail`])},[Me("span",{class:U(`${e.prefixCls}__twrap`)},[e.showDetailBack?(T(),m("span",{key:0,onClick:o[0]||(o[0]=(...c)=>e.handleClose&&e.handleClose(...c))},[s(u,{class:U(`${e.prefixCls}__back`)},null,8,["class"])])):x("",!0),e.title?(T(),m("span",mt,re(e.title),1)):x("",!0)],2),Me("span",{class:U(`${e.prefixCls}__toolbar`)},[D(e.$slots,"titleToolbar")],2)],2)):(T(),j(a,{key:0,class:U(e.prefixCls)},{default:M(()=>[D(e.$slots,"title"),Oe(" "+re(e.$slots.title?"":e.title),1)]),_:3},8,["class"]))}const Ft=le(Ut,[["render",Pt]]),kt=q({components:{Drawer:Rt,ScrollContainer:st,DrawerFooter:wt,DrawerHeader:Ft},inheritAttrs:!1,props:ht,emits:["visible-change","ok","close","register"],setup(e,{emit:o}){const i=Q(!1),g=it(),d=Q(null),{t:O}=ae(),{prefixVar:a,prefixCls:u}=Se("basic-drawer"),c={setDrawerProps:J,emitVisible:void 0},p=me();p&&o("register",c,p.uid);const A=C(()=>De(K(e),t(d))),f=C(()=>{const l=ce(y(y({placement:"right"},t(g)),t(A)),{visible:t(i)});l.title=void 0;const{isDetail:E,width:W,wrapClassName:$,getContainer:_e}=l;if(E){W||(l.width="100%");const te=`${u}__detail`;l.class=$?`${$} ${te}`:te,_e||(l.getContainer=`.${a}-layout-content`)}return l}),k=C(()=>y(y({},g),t(f))),z=C(()=>{const{footerHeight:l,showFooter:E}=t(f);return E&&l?rt(l)?`${l}px`:`${l.replace("px","")}px`:"0px"}),Z=C(()=>({position:"relative",height:`calc(100% - ${t(z)})`})),ue=C(()=>{var l;return!!((l=t(f))!=null&&l.loading)});Ne(()=>e.visible,(l,E)=>{l!==E&&(i.value=l)},{deep:!0}),Ne(()=>i.value,l=>{Tt(()=>{o("visible-change",l)})});function G(l){return fe(this,null,function*(){const{closeFunc:E}=t(f);if(o("close",l),E&&at(E)){const W=yield E();i.value=!W;return}i.value=!1})}function J(l){d.value=De(t(d)||{},l),Reflect.has(l,"visible")&&(i.value=!!l.visible)}function ee(){o("ok")}return{onClose:G,t:O,prefixCls:u,getMergeProps:A,getScrollContentStyle:Z,getProps:f,getLoading:ue,getBindValues:k,getFooterHeight:z,handleOk:ee}}});function Gt(e,o,i,g,d,O){const a=N("DrawerHeader"),u=N("ScrollContainer"),c=N("DrawerFooter"),p=N("Drawer"),A=ft("loading");return T(),j(p,Y({class:e.prefixCls,onClose:e.onClose},e.getBindValues),Re({default:M(()=>[Dt((T(),j(u,{style:Ue(e.getScrollContentStyle),"loading-tip":e.loadingText||e.t("common.loadingText")},{default:M(()=>[D(e.$slots,"default")]),_:3},8,["style","loading-tip"])),[[A,e.getLoading]]),s(c,Y(e.getProps,{onClose:e.onClose,onOk:e.handleOk,height:e.getFooterHeight}),Re({_:2},[At(Object.keys(e.$slots),f=>({name:f,fn:M(k=>[D(e.$slots,f,Ct(Mt(k||{})))])}))]),1040,["onClose","onOk","height"])]),_:2},[e.$slots.title?{name:"title",fn:M(()=>[D(e.$slots,"title")]),key:"1"}:{name:"title",fn:M(()=>[s(a,{title:e.getMergeProps.title,isDetail:e.isDetail,showDetailBack:e.showDetailBack,onClose:e.onClose},{titleToolbar:M(()=>[D(e.$slots,"titleToolbar")]),_:3},8,["title","isDetail","showDetailBack","onClose"])]),key:"0"}]),1040,["class","onClose"])}const Wt=le(kt,[["render",Gt]]),v=Pe({}),ye=Pe({});function $t(){if(!me())throw new Error("useDrawer() can only be used inside setup() or functional components!");const e=Q(null),o=Q(!1),i=Q("");function g(a,u){lt(()=>{e.value=null,o.value=null,v[t(i)]=null}),!(t(o)&&ut()&&a===t(e))&&(i.value=u,e.value=a,o.value=!0,a.emitVisible=(c,p)=>{ye[p]=c})}const d=()=>{const a=t(e);return a||_t("useDrawer instance is undefined!"),a},O={setDrawerProps:a=>{var u;(u=d())==null||u.setDrawerProps(a)},getVisible:C(()=>ye[~~t(i)]),openDrawer:(a=!0,u,c=!0)=>{var A;if((A=d())==null||A.setDrawerProps({visible:a}),!u)return;if(c){v[t(i)]=null,v[t(i)]=K(u);return}It(K(v[t(i)]),K(u))||(v[t(i)]=K(u))},closeDrawer:()=>{var a;(a=d())==null||a.setDrawerProps({visible:!1})}};return[g,O]}const vt=ct(Wt),Ht=F(()=>P(()=>import("./TypePicker-d0473e98.js"),["assets/TypePicker-d0473e98.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/TypePicker-e8660c9e.css"]));F(()=>P(()=>import("./ThemeColorPicker-b1fde352.js"),["assets/ThemeColorPicker-b1fde352.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/index-fd2c8e81.js","assets/index-929a8618.js","assets/useContentViewHeight-85a83bf4.js","assets/useWindowSizeFn-7036f6b8.js","assets/index-054645fa.css","assets/uniqBy-401d319d.js","assets/lock-012d98e6.js","assets/index-a190d313.css","assets/ThemeColorPicker-84b84c57.css"]));const Xt=F(()=>P(()=>import("./SettingFooter-6d8fb564.js"),["assets/SettingFooter-6d8fb564.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/SettingFooter-22472c83.css"])),_=F(()=>P(()=>import("./SwitchItem-031a6f80.js"),["assets/SwitchItem-031a6f80.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/index-fd2c8e81.js","assets/index-929a8618.js","assets/useContentViewHeight-85a83bf4.js","assets/useWindowSizeFn-7036f6b8.js","assets/index-054645fa.css","assets/uniqBy-401d319d.js","assets/lock-012d98e6.js","assets/index-a190d313.css","assets/SwitchItem-e293bb1b.css"])),H=F(()=>P(()=>import("./SelectItem-a690ef06.js"),["assets/SelectItem-a690ef06.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/index-fd2c8e81.js","assets/index-929a8618.js","assets/useContentViewHeight-85a83bf4.js","assets/useWindowSizeFn-7036f6b8.js","assets/index-054645fa.css","assets/uniqBy-401d319d.js","assets/lock-012d98e6.js","assets/index-a190d313.css","assets/SelectItem-acbd08e7.css"])),be=F(()=>P(()=>import("./InputNumberItem-43a8a6c9.js"),["assets/InputNumberItem-43a8a6c9.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/index-fd2c8e81.js","assets/index-929a8618.js","assets/useContentViewHeight-85a83bf4.js","assets/useWindowSizeFn-7036f6b8.js","assets/index-054645fa.css","assets/uniqBy-401d319d.js","assets/lock-012d98e6.js","assets/index-a190d313.css","assets/InputNumberItem-9fb89c8f.css"])),{t:S}=ae();var n=(e=>(e[e.CHANGE_LAYOUT=0]="CHANGE_LAYOUT",e[e.CHANGE_THEME_COLOR=1]="CHANGE_THEME_COLOR",e[e.CHANGE_THEME=2]="CHANGE_THEME",e[e.MENU_HAS_DRAG=3]="MENU_HAS_DRAG",e[e.MENU_ACCORDION=4]="MENU_ACCORDION",e[e.MENU_TRIGGER=5]="MENU_TRIGGER",e[e.MENU_TOP_ALIGN=6]="MENU_TOP_ALIGN",e[e.MENU_COLLAPSED=7]="MENU_COLLAPSED",e[e.MENU_COLLAPSED_SHOW_TITLE=8]="MENU_COLLAPSED_SHOW_TITLE",e[e.MENU_WIDTH=9]="MENU_WIDTH",e[e.MENU_SHOW_SIDEBAR=10]="MENU_SHOW_SIDEBAR",e[e.MENU_THEME=11]="MENU_THEME",e[e.MENU_SPLIT=12]="MENU_SPLIT",e[e.MENU_FIXED=13]="MENU_FIXED",e[e.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE=14]="MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE",e[e.MENU_TRIGGER_MIX_SIDEBAR=15]="MENU_TRIGGER_MIX_SIDEBAR",e[e.MENU_FIXED_MIX_SIDEBAR=16]="MENU_FIXED_MIX_SIDEBAR",e[e.HEADER_SHOW=17]="HEADER_SHOW",e[e.HEADER_THEME=18]="HEADER_THEME",e[e.HEADER_FIXED=19]="HEADER_FIXED",e[e.HEADER_SEARCH=20]="HEADER_SEARCH",e[e.TABS_SHOW_QUICK=21]="TABS_SHOW_QUICK",e[e.TABS_SHOW_REDO=22]="TABS_SHOW_REDO",e[e.TABS_SHOW=23]="TABS_SHOW",e[e.TABS_SHOW_FOLD=24]="TABS_SHOW_FOLD",e[e.LOCK_TIME=25]="LOCK_TIME",e[e.FULL_CONTENT=26]="FULL_CONTENT",e[e.CONTENT_MODE=27]="CONTENT_MODE",e[e.SHOW_BREADCRUMB=28]="SHOW_BREADCRUMB",e[e.SHOW_BREADCRUMB_ICON=29]="SHOW_BREADCRUMB_ICON",e[e.GRAY_MODE=30]="GRAY_MODE",e[e.COLOR_WEAK=31]="COLOR_WEAK",e[e.SHOW_LOGO=32]="SHOW_LOGO",e[e.SHOW_FOOTER=33]="SHOW_FOOTER",e[e.ROUTER_TRANSITION=34]="ROUTER_TRANSITION",e[e.OPEN_PROGRESS=35]="OPEN_PROGRESS",e[e.OPEN_PAGE_LOADING=36]="OPEN_PAGE_LOADING",e[e.OPEN_ROUTE_TRANSITION=37]="OPEN_ROUTE_TRANSITION",e))(n||{});const Vt=[{value:Ae.FULL,label:S("layout.setting.contentModeFull")},{value:Ae.FIXED,label:S("layout.setting.contentModeFixed")}],Kt=[{value:de.CENTER,label:S("layout.setting.topMenuAlignRight")},{value:de.START,label:S("layout.setting.topMenuAlignLeft")},{value:de.END,label:S("layout.setting.topMenuAlignCenter")}],xt=e=>[{value:ie.NONE,label:S("layout.setting.menuTriggerNone")},{value:ie.FOOTER,label:S("layout.setting.menuTriggerBottom")},...e?[]:[{value:ie.HEADER,label:S("layout.setting.menuTriggerTop")}]],Qt=[B.ZOOM_FADE,B.FADE,B.ZOOM_OUT,B.FADE_SIDE,B.FADE_BOTTOM,B.FADE_SCALE].map(e=>({label:e,value:e})),jt=[{title:S("layout.setting.menuTypeSidebar"),mode:se.INLINE,type:X.SIDEBAR},{title:S("layout.setting.menuTypeMix"),mode:se.INLINE,type:X.MIX},{title:S("layout.setting.menuTypeTopMenu"),mode:se.HORIZONTAL,type:X.TOP_MENU},{title:S("layout.setting.menuTypeMixSidebar"),mode:se.INLINE,type:X.MIX_SIDEBAR}],Yt=[{value:Ce.HOVER,label:S("layout.setting.triggerHover")},{value:Ce.CLICK,label:S("layout.setting.triggerClick")}];function qt(e,o){const i=he(),g=zt(e,o);i.setProjectConfig(g),e===n.CHANGE_THEME&&(Le(),Be())}function zt(e,o){const i=he(),{getThemeColor:g,getDarkMode:d}=we();switch(e){case n.CHANGE_LAYOUT:const{mode:O,type:a,split:u}=o;return{menuSetting:y({mode:O,type:a,collapsed:!1,show:!0,hidden:!1},u===void 0?{split:u}:{})};case n.CHANGE_THEME_COLOR:return g.value===o?{}:{themeColor:o};case n.CHANGE_THEME:return d.value===o?{}:(Ot(o),{});case n.MENU_HAS_DRAG:return{menuSetting:{canDrag:o}};case n.MENU_ACCORDION:return{menuSetting:{accordion:o}};case n.MENU_TRIGGER:return{menuSetting:{trigger:o}};case n.MENU_TOP_ALIGN:return{menuSetting:{topMenuAlign:o}};case n.MENU_COLLAPSED:return{menuSetting:{collapsed:o}};case n.MENU_WIDTH:return{menuSetting:{menuWidth:o}};case n.MENU_SHOW_SIDEBAR:return{menuSetting:{show:o}};case n.MENU_COLLAPSED_SHOW_TITLE:return{menuSetting:{collapsedShowTitle:o}};case n.MENU_THEME:return Be(o),{menuSetting:{bgColor:o}};case n.MENU_SPLIT:return{menuSetting:{split:o}};case n.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE:return{menuSetting:{closeMixSidebarOnChange:o}};case n.MENU_FIXED:return{menuSetting:{fixed:o}};case n.MENU_TRIGGER_MIX_SIDEBAR:return{menuSetting:{mixSideTrigger:o}};case n.MENU_FIXED_MIX_SIDEBAR:return{menuSetting:{mixSideFixed:o}};case n.OPEN_PAGE_LOADING:return i.setPageLoading(!1),{transitionSetting:{openPageLoading:o}};case n.ROUTER_TRANSITION:return{transitionSetting:{basicTransition:o}};case n.OPEN_ROUTE_TRANSITION:return{transitionSetting:{enable:o}};case n.OPEN_PROGRESS:return{transitionSetting:{openNProgress:o}};case n.LOCK_TIME:return{lockTime:o};case n.FULL_CONTENT:return{fullContent:o};case n.CONTENT_MODE:return{contentMode:o};case n.SHOW_BREADCRUMB:return{showBreadCrumb:o};case n.SHOW_BREADCRUMB_ICON:return{showBreadCrumbIcon:o};case n.GRAY_MODE:return dt(o),{grayMode:o};case n.SHOW_FOOTER:return{showFooter:o};case n.COLOR_WEAK:return gt(o),{colorWeak:o};case n.SHOW_LOGO:return{showLogo:o};case n.TABS_SHOW_QUICK:return{multiTabsSetting:{showQuick:o}};case n.TABS_SHOW:return{multiTabsSetting:{show:o}};case n.TABS_SHOW_REDO:return{multiTabsSetting:{showRedo:o}};case n.TABS_SHOW_FOLD:return{multiTabsSetting:{showFold:o}};case n.HEADER_THEME:return Le(o),{headerSetting:{bgColor:o}};case n.HEADER_SEARCH:return{headerSetting:{showSearch:o}};case n.HEADER_FIXED:return{headerSetting:{fixed:o}};case n.HEADER_SHOW:return{headerSetting:{show:o}};default:return{}}}const{t:r}=ae(),Zt=q({name:"SettingDrawer",setup(e,{attrs:o}){const{getContentMode:i,getShowFooter:g,getShowBreadCrumb:d,getShowBreadCrumbIcon:O,getShowLogo:a,getFullContent:u,getColorWeak:c,getGrayMode:p,getLockTime:A,getShowDarkModeToggle:f}=we(),{getOpenPageLoading:k,getBasicTransition:z,getEnableTransition:Z,getOpenNProgress:ue}=St(),{getIsHorizontal:G,getShowMenu:J,getMenuType:ee,getTrigger:l,getCollapsedShowTitle:E,getMenuFixed:W,getCollapsed:$,getCanDrag:_e,getTopMenuAlign:te,getAccordion:ke,getMenuWidth:Ge,getIsTopMenu:We,getSplit:oe,getIsMixSidebar:R,getCloseMixSidebarOnChange:$e,getMixSideTrigger:ve,getMixSideFixed:He}=pt(),{getShowHeader:b,getFixed:Xe,getShowSearch:Ve}=yt(),{getShowMultipleTab:ne,getShowQuick:Ke,getShowRedo:xe,getShowFold:Qe}=bt(),I=C(()=>t(J)&&!t(G));function je(){return s(V,null,[s(Ht,{menuTypeList:jt,handler:h=>{qt(n.CHANGE_LAYOUT,{mode:h.mode,type:h.type,split:t(G)?!1:void 0})},def:t(ee)},null)])}function Ye(){let h=t(l);const pe=xt(t(oe));return pe.some(L=>L.value===h)||(h=ie.FOOTER),s(V,null,[s(_,{title:r("layout.setting.splitMenu"),event:n.MENU_SPLIT,def:t(oe),disabled:!t(I)||t(ee)!==X.MIX},null),s(_,{title:r("layout.setting.mixSidebarFixed"),event:n.MENU_FIXED_MIX_SIDEBAR,def:t(He),disabled:!t(R)},null),s(_,{title:r("layout.setting.closeMixSidebarOnChange"),event:n.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE,def:t($e),disabled:!t(R)},null),s(_,{title:r("layout.setting.menuCollapse"),event:n.MENU_COLLAPSED,def:t($),disabled:!t(I)},null),s(_,{title:r("layout.setting.menuDrag"),event:n.MENU_HAS_DRAG,def:t(_e),disabled:!t(I)},null),s(_,{title:r("layout.setting.menuSearch"),event:n.HEADER_SEARCH,def:t(Ve),disabled:!t(b)},null),s(_,{title:r("layout.setting.menuAccordion"),event:n.MENU_ACCORDION,def:t(ke),disabled:!t(I)},null),s(_,{title:r("layout.setting.collapseMenuDisplayName"),event:n.MENU_COLLAPSED_SHOW_TITLE,def:t(E),disabled:!t(I)||!t($)||t(R)},null),s(_,{title:r("layout.setting.fixedHeader"),event:n.HEADER_FIXED,def:t(Xe),disabled:!t(b)},null),s(_,{title:r("layout.setting.fixedSideBar"),event:n.MENU_FIXED,def:t(W),disabled:!t(I)||t(R)},null),s(H,{title:r("layout.setting.mixSidebarTrigger"),event:n.MENU_TRIGGER_MIX_SIDEBAR,def:t(ve),options:Yt,disabled:!t(R)},null),s(H,{title:r("layout.setting.topMenuLayout"),event:n.MENU_TOP_ALIGN,def:t(te),options:Kt,disabled:!t(b)||t(oe)||!t(We)&&!t(oe)||t(R)},null),s(H,{title:r("layout.setting.menuCollapseButton"),event:n.MENU_TRIGGER,def:h,options:pe,disabled:!t(I)||t(R)},null),s(H,{title:r("layout.setting.contentMode"),event:n.CONTENT_MODE,def:t(i),options:Vt},null),s(be,{title:r("layout.setting.autoScreenLock"),min:0,event:n.LOCK_TIME,defaultValue:t(A),formatter:L=>parseInt(L)===0?`0(${r("layout.setting.notAutoScreenLock")})`:`${L}${r("layout.setting.minute")}`},null),s(be,{title:r("layout.setting.expandedMenuWidth"),max:600,min:100,step:10,event:n.MENU_WIDTH,disabled:!t(I),defaultValue:t(Ge),formatter:L=>`${parseInt(L)}px`},null)])}function qe(){return s(V,null,[s(_,{title:r("layout.setting.breadcrumb"),event:n.SHOW_BREADCRUMB,def:t(d),disabled:!t(b)},null),s(_,{title:r("layout.setting.breadcrumbIcon"),event:n.SHOW_BREADCRUMB_ICON,def:t(O),disabled:!t(b)},null),s(_,{title:r("layout.setting.tabs"),event:n.TABS_SHOW,def:t(ne)},null),s(_,{title:r("layout.setting.tabsRedoBtn"),event:n.TABS_SHOW_REDO,def:t(xe),disabled:!t(ne)},null),s(_,{title:r("layout.setting.tabsQuickBtn"),event:n.TABS_SHOW_QUICK,def:t(Ke),disabled:!t(ne)},null),s(_,{title:r("layout.setting.tabsFoldBtn"),event:n.TABS_SHOW_FOLD,def:t(Qe),disabled:!t(ne)},null),s(_,{title:r("layout.setting.sidebar"),event:n.MENU_SHOW_SIDEBAR,def:t(J),disabled:t(G)},null),s(_,{title:r("layout.setting.header"),event:n.HEADER_SHOW,def:t(b)},null),s(_,{title:"Logo",event:n.SHOW_LOGO,def:t(a),disabled:t(R)},null),s(_,{title:r("layout.setting.footer"),event:n.SHOW_FOOTER,def:t(g)},null),s(_,{title:r("layout.setting.fullContent"),event:n.FULL_CONTENT,def:t(u)},null),s(_,{title:r("layout.setting.grayMode"),event:n.GRAY_MODE,def:t(p)},null),s(_,{title:r("layout.setting.colorWeak"),event:n.COLOR_WEAK,def:t(c)},null)])}function ze(){return s(V,null,[s(_,{title:r("layout.setting.progress"),event:n.OPEN_PROGRESS,def:t(ue)},null),s(_,{title:r("layout.setting.switchLoading"),event:n.OPEN_PAGE_LOADING,def:t(k)},null),s(_,{title:r("layout.setting.switchAnimation"),event:n.OPEN_ROUTE_TRANSITION,def:t(Z)},null),s(H,{title:r("layout.setting.animationType"),event:n.ROUTER_TRANSITION,def:t(z),options:Qt,disabled:!t(Z)},null)])}return()=>s(vt,Y(o,{title:r("layout.setting.drawerTitle"),width:330,class:"setting-drawer"}),{default:()=>[t(f)&&s(w,null,{default:()=>r("layout.setting.darkMode")}),s(w,null,{default:()=>r("layout.setting.navMode")}),je(),s(w,null,{default:()=>r("layout.setting.interfaceFunction")}),Ye(),s(w,null,{default:()=>r("layout.setting.interfaceDisplay")}),qe(),s(w,null,{default:()=>r("layout.setting.animation")}),ze(),s(w,null,null),s(Xt,null,null)]})}}),Jt=q({name:"SettingButton",components:{SettingDrawer:Zt,Icon:Et},setup(){const[e,{openDrawer:o}]=$t();return{register:e,openDrawer:o}}});function eo(e,o,i,g,d,O){const a=N("Icon"),u=N("SettingDrawer");return T(),m("div",{onClick:o[0]||(o[0]=c=>e.openDrawer(!0))},[s(a,{icon:"ion:settings-outline"}),s(u,{onRegister:e.register},null,8,["onRegister"])])}const to=le(Jt,[["render",eo]]),lo=Object.freeze(Object.defineProperty({__proto__:null,default:to},Symbol.toStringTag,{value:"Module"}));export{qt as b,lo as i};
