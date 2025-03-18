use std::ffi::CString;

#[no_mangle]
#[allow(non_snake_case)]
pub extern fn sayHello() -> *const i8 {
    let c_str = CString::new("I WOKRED BROTHAAAAA").unwrap();
    return c_str.into_raw();
}