import{a2 as d,ac as k,aE as B,u as L,aj as S,_ as y}from"./index.js";import{c as f,u as D}from"./index-fd2c8e81.js";import P from"./SessionTimeoutLogin-fcab78ce.js";import{aM as w}from"./antd-685f22a1.js";import{d as C,e as T,u as o,a7 as n,_ as s,$ as E,f as F,a8 as u,ab as c,a2 as I,F as v}from"./vue-50936e44.js";import"./index-929a8618.js";import"./useContentViewHeight-85a83bf4.js";import"./useWindowSizeFn-7036f6b8.js";import"./uniqBy-401d319d.js";import"./lock-012d98e6.js";import"./Login.vue_vue_type_style_index_0_lang-468beee5.js";import"./LoginForm.vue_vue_type_style_index_0_lang-ef231f43.js";import"./LoginFormTitle.vue_vue_type_script_setup_true_lang-64884d13.js";const O=C({name:"LayoutFeatures",components:{BackTop:w,LayoutLockPage:f(()=>d(()=>import("./index-2a8e0f8c.js"),["assets/index-2a8e0f8c.js","assets/vue-50936e44.js","assets/LockPage-2665687e.js","assets/index.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/lock-012d98e6.js","assets/header-55b09394.js","assets/LockPage-b0b08e00.css"])),SettingDrawer:f(()=>d(()=>import("./index-792b1951.js").then(e=>e.i),["assets/index-792b1951.js","assets/index.js","assets/vue-50936e44.js","assets/antd-685f22a1.js","assets/index-47f1e006.css","assets/index-fd2c8e81.js","assets/index-929a8618.js","assets/useContentViewHeight-85a83bf4.js","assets/useWindowSizeFn-7036f6b8.js","assets/index-054645fa.css","assets/uniqBy-401d319d.js","assets/lock-012d98e6.js","assets/index-a190d313.css","assets/index-6c94692d.css"])),SessionTimeoutLogin:P},setup(){const{getUseOpenBackTop:e,getShowSettingButton:m,getSettingButtonPosition:p,getFullContent:g}=k(),_=B(),{prefixCls:l}=L("setting-drawer-feature"),{getShowHeader:r}=D(),a=T(()=>_.getSessionTimeout),i=T(()=>{if(!o(m))return!1;const t=o(p);return t===S.AUTO?!o(r)||o(g):t===S.FIXED});return{getTarget:()=>document.body,getUseOpenBackTop:e,getIsFixedSettingDrawer:i,prefixCls:l,getIsSessionTimeout:a}}});function h(e,m,p,g,_,l){const r=n("LayoutLockPage"),a=n("BackTop"),i=n("SettingDrawer"),t=n("SessionTimeoutLogin");return s(),E(v,null,[F(r),e.getUseOpenBackTop?(s(),u(a,{key:0,target:e.getTarget},null,8,["target"])):c("",!0),e.getIsFixedSettingDrawer?(s(),u(i,{key:1,class:I(e.prefixCls)},null,8,["class"])):c("",!0),e.getIsSessionTimeout?(s(),u(t,{key:2})):c("",!0)],64)}const X=y(O,[["render",h]]);export{X as default};