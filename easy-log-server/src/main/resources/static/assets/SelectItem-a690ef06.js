import{u as l,_ as i}from "./index.js";import{b as r}from "./index-792b1951.js";import{d,e as p,a7 as m,_ as c,$ as u,a0 as f,a1 as _,f as g,ac as y,a2 as C}from "./vue-50936e44.js";import{ae as S}from "./antd-685f22a1.js";import"./index-fd2c8e81.js";import"./index-929a8618.js";import"./useContentViewHeight-85a83bf4.js";import"./useWindowSizeFn-7036f6b8.js";import"./uniqBy-401d319d.js";import"./lock-012d98e6.js";const b=d({name:"SelectItem",components:{Select:S},props:{event:{type:Number},disabled:{type:Boolean},title:{type:String},def:{type:[String,Number]},initValue:{type:[String,Number]},options:{type:Array,default:()=>[]}},setup(e){const{prefixCls:t}=l("setting-select-item"),a=p(()=>e.def?{value:e.def,defaultValue:e.initValue||e.def}:{});function n(s){e.event&&r(e.event,s)}return{prefixCls:t,handleChange:n,getBindValue:a}}});function v(e, t, a, n, s, h){const o=m("Select");return c(),u("div",{class:C(e.prefixCls)},[f("span",null,_(e.title),1),g(o,y(e.getBindValue,{class:`${e.prefixCls}-select`,onChange:e.handleChange,disabled:e.disabled,size:"small",options:e.options}),null,16,["class","onChange","disabled","options"])],2)}const H=i(b,[["render",v],["__scopeId","data-v-d811fa90"]]);export{H as default};