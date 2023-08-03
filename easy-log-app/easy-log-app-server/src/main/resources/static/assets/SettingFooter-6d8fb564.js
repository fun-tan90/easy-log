import{a6 as k,u as b,ak as h,au as R,af as O,d as M,b5 as v,at as f,aP as B,aQ as T,aR as $,aI as A,_ as P}from"./index.js";import{ba as F,as as I}from"./antd-685f22a1.js";import{d as N,u as m,a7 as c,_ as j,$ as w,f as o,a9 as i,E as u,a1 as d,a2 as x}from"./vue-50936e44.js";const D=N({name:"SettingFooter",components:{CopyOutlined:F,RedoOutlined:I},setup(){const e=k(),{prefixCls:p}=b("setting-footer"),{t:a}=M(),{createSuccessModal:g,createMessage:r}=A(),C=h(),l=R(),t=O();function n(){const{isSuccessRef:s}=v(JSON.stringify(m(t.getProjectConfig),null,2));m(s)&&g({title:a("layout.setting.operatingTitle"),content:a("layout.setting.operatingContent")})}function S(){try{t.setProjectConfig(f);const{colorWeak:s,grayMode:y}=f;B(),T(s),$(y),r.success(a("layout.setting.resetSuccess"))}catch(s){r.error(s)}}function _(){localStorage.clear(),t.resetAllState(),e.resetState(),C.resetState(),l.resetState(),location.reload()}return{prefixCls:p,t:a,handleCopy:n,handleResetSetting:S,handleClearAndRedo:_}}});function E(e,p,a,g,r,C){const l=c("CopyOutlined"),t=c("a-button"),n=c("RedoOutlined");return j(),w("div",{class:x(e.prefixCls)},[o(t,{type:"primary",block:"",onClick:e.handleCopy},{default:i(()=>[o(l,{class:"mr-2"}),u(" "+d(e.t("layout.setting.copyBtn")),1)]),_:1},8,["onClick"]),o(t,{color:"warning",block:"",onClick:e.handleResetSetting,class:"my-3"},{default:i(()=>[o(n,{class:"mr-2"}),u(" "+d(e.t("common.resetText")),1)]),_:1},8,["onClick"]),o(t,{color:"error",block:"",onClick:e.handleClearAndRedo},{default:i(()=>[o(n,{class:"mr-2"}),u(" "+d(e.t("layout.setting.clearBtn")),1)]),_:1},8,["onClick"])],2)}const G=P(D,[["render",E],["__scopeId","data-v-7282eaa7"]]);export{G as default};