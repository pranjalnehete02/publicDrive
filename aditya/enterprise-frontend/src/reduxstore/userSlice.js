import { createSlice } from '@reduxjs/toolkit'

export const userSlice = createSlice({
  name: 'user',
  initialState: {
    user: null
  },
  reducers: {
    addUser: (state, action) => {
      state.user = action.payload
    },
    deleteUser: (state)=>{
        state.user = null;
    }
  },
})


export const { addUser, deleteUser } = userSlice.actions

export default userSlice.reducer