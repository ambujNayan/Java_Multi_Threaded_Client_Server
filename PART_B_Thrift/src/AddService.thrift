service AddService {
 
 
   i32 CreateAccount(),
   bool Deposit(i32 UID, i32 amount),
   i32 GetBalance(i32 UID),
   bool Transfer(i32 sourceUID, i32 targetUID, i32 amount)
}
