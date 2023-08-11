import { useCallback, useState } from "react";

export default function RegEmail({ children }) {
  const [Email, SetEmail] = useState("");

  const onChange = useCallback((e) => {
    SetEmail(e.target.value);
  }, []);

  return (
    <div className="Div">
      <label className="Label" htmlFor="RegInput">
        이메일
      </label>

      <br />

      <input
        id="RegInput"
        type="text"
        name="Email"
        value={Email}
        onChange={onChange}
        placeholder="이메일을 입력해주세요"
      />

      {children}
    </div>
  );
}
