import { useState, useCallback } from "react";

export default function Password() {
  const [Password, SetPassword] = useState("");

  const onChange = useCallback((e) => {
    SetPassword(e.target.value);
  }, []);

  return (
    <div className="Div">
      <label className="Label" htmlFor="Password">
        비밀번호
      </label>

      <br />

      <input
        className="Input"
        id="Password"
        type="password"
        value={Password}
        onChange={onChange}
        placeholder="비밀번호을 입력해주세요"
      />
    </div>
  );
}
